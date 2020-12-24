package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.*;
import ba.codecta.game.repository.entity.*;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.MapDto;
import ba.codecta.game.services.model.MapDungeonDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@Transactional
public class MapServiceImpl implements MapService {

    @Inject
    MapRepository mapRepository;

    @Inject
    MapDungeonRepository mapDungeonRepository;

    @Inject
    DungeonService dungeonService;

    @Inject
    MonsterService monsterService;

    @Inject
    ItemService itemService;

    @Inject
    InventoryService inventoryService;

    @Inject
    HeroService heroService;

    /**
     * Creates map and initializes dungeons
     * @param levelWeightFactor - level number
     * @return MapDto object
     */
    @Override
    public MapDto createMap(Integer levelWeightFactor) {
        MapEntity newMap = new MapEntity(2 + levelWeightFactor, 3);
        newMap = mapRepository.add(newMap);

        List<DungeonEntity> dungeons = dungeonService.getAllDungeons();
        List<MonsterEntity> monsters = monsterService.getAllMonsters();
        List<ItemEntity> items = itemService.getAllItems();

        boolean isKeyGivenToMonster = false;
        this.addMapDungeon(newMap, dungeons.get(0), null, null, null,0, 0);
        for(int i = 0; i < 2 + levelWeightFactor; ++i){
            for(int j = 0; j < 3; ++j){
                if(i == 0 && j == 0){
                    continue; // Lobby
                }
                if((i == 1 + levelWeightFactor && j == 2)){
                    break; // Skip because this is boss dungeon
                }
                MonsterEntity monster = null;
                ItemEntity monsterItem = null;
                DungeonEntity dungeon = dungeons.get(new Random().nextInt(dungeons.size() - 2) + 2);
                if(new Random().nextInt(100) > 40 || (!isKeyGivenToMonster && (i == 1 + levelWeightFactor && j == 2))){
                    monster = monsters.get(new Random().nextInt(monsters.size() - 1) + 1);
                    monsterItem = items.get(new Random().nextInt(items.size() - 2) + 2);
                    if(!isKeyGivenToMonster){
                        if(new Random().nextInt() > 40 || (i == 1 + levelWeightFactor && j == 2)){
                            monsterItem = items.get(1);
                            isKeyGivenToMonster = true;
                        }
                    }
                }
                ItemEntity secretItem = items.get(new Random().nextInt(items.size() - 2) + 2);
                this.addMapDungeon(newMap, dungeon, monster, monsterItem, secretItem,i, j);
            }
        }
        this.addMapDungeon(newMap, dungeons.get(1), monsters.get(0), items.get(0), null,1 + levelWeightFactor, 2);

        List<String> actions = Arrays.asList("Move Down", "Move right", "Shop");
        return this.createMapDto(newMap.getId(),"Game has started, good luck on your quest adventurer!", actions);
    }

    /**
     * Handles move action
     * @param mapId - map id
     * @param moveDirection - move direction
     * @return message
     */
    @Override
    public String move(Integer mapId, MoveDirection moveDirection) {
        MapEntity currentMap = this.getMap(mapId);
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);

        if((moveDirection == MoveDirection.UP && currentMap.getPlayerLocationX() == 0) || (moveDirection == MoveDirection.DOWN && currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1) ||
                (moveDirection == MoveDirection.LEFT && currentMap.getPlayerLocationY() == 0) || (moveDirection == MoveDirection.RIGHT && currentMap.getPlayerLocationY() == currentMap.getMapDimensionY() - 1)){
            return "Invalid move! You hit the wall";
        }
        if(!currentDungeon.isMonsterFriend()){
            return "You need to interact with the monster first";
        }

        if(!currentMap.isPlayerHasKey() && ((currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1 && currentMap.getPlayerLocationY() == 1) || (currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 2 && currentMap.getPlayerLocationY() == 2)) && (moveDirection == MoveDirection.DOWN || moveDirection == MoveDirection.RIGHT)){
            return "You don't have the key";
        }

        String message = "";
        if(moveDirection == MoveDirection.UP){
            currentMap.setPlayerLocationX(currentMap.getPlayerLocationX() - 1);
            message = "You moved up";
        }
        if(moveDirection == MoveDirection.DOWN){
            currentMap.setPlayerLocationX(currentMap.getPlayerLocationX() + 1);
            message = "You moved down";
        }
        if(moveDirection == MoveDirection.RIGHT){
            currentMap.setPlayerLocationY(currentMap.getPlayerLocationY() + 1);
            message = "You moved right";
        }
        if(moveDirection == MoveDirection.LEFT){
            currentMap.setPlayerLocationY(currentMap.getPlayerLocationY() - 1);
            message = "You moved left";
        }

        mapRepository.save(currentMap);
        MapDungeonEntity newDungeon = getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);
        newDungeon.setVisited(true);
        mapDungeonRepository.save(newDungeon);
        return message;
    }

    /**
     * Handles player action
     * @param heroId - id of hero
     * @param mapId - map id
     * @param mapAction - action
     * @return MapDto Object
     */
    @Override
    public String action(Integer heroId, Integer mapId, MapAction mapAction) {
        MapEntity currentMap = this.getMap(mapId);
        HeroEntity hero = heroService.getHeroEntityById(heroId);
        MapEntity map = this.getMap(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentPlayerDungeonLocation(mapId, map.getPlayerLocationX(), map.getPlayerLocationY());

        if((mapAction == MapAction.BEFRIEND && currentDungeon.getMonster() == null) || (mapAction == MapAction.FIGHT && currentDungeon.getMonster() == null)
        || (mapAction == MapAction.FLEE && currentDungeon.getMonster() == null)){
            return "Invalid action! There is no monster in this dungeon!";
        }

        // FIGHT
        if(mapAction == MapAction.FIGHT){
            return this.actionFight(currentDungeon, currentMap, hero);
        }

        // SEARCH SECRET ITEM
        if(mapAction == MapAction.SEARCH_SECRET_ITEM){
            return this.actionSearchSecretItem(currentDungeon, currentMap, hero);
        }

        // BEFRIEND MONSTER
        if(mapAction == MapAction.BEFRIEND){
            return this.actionBefriend(currentDungeon, currentMap, hero);
        }

        // FLEE
        if(mapAction == MapAction.FLEE){
            return this.actionFlee(currentDungeon, currentMap, hero);
        }


        return "Something went wrong";
    }

    /**
     * Gets current map state
     * @param mapId - map id
     * @param message - message to be displayed
     * @return MapDto object
     */
    @Override
    public MapDto getStatus(Integer mapId, String message) {
        MapEntity currentMap = this.getMap(mapId);
        return this.createMapDto(mapId, message, this.createActions(currentMap));
    }

    /**
     * Method that creates MapDto
     * @param message - Info message
     * @param actions - Actions that player can do
     * @return Map Dto Object
     */
    private MapDto createMapDto(Integer mapId, String message, List<String> actions){
        MapEntity mapFromDb = this.getMap(mapId);
        ModelMapper modelMapper = new ModelMapper();
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonDto currentPlayerDungeon = modelMapper.map(this.getCurrentDungeonPlayerLocation(mapFromDb.getPlayerLocationX(), mapFromDb.getPlayerLocationY(), mappedDungeons), MapDungeonDto.class);
        MapDto result = new MapDto(mapId, message, actions, mapFromDb.getPlayerLocationX(), mapFromDb.getPlayerLocationY(), mapFromDb.isPlayerHasKey(), mapFromDb.getMapDimensionX(), mapFromDb.getMapDimensionY(), currentPlayerDungeon);
        List<MapDungeonDto> mapDungeonDtos = new ArrayList<>();
        for(MapDungeonEntity dungeons : mappedDungeons){
            mapDungeonDtos.add(modelMapper.map(dungeons, MapDungeonDto.class));
        }
        result.setDungeons(mapDungeonDtos);

        return result;
    }

    /**
     * Method adds dungeon to map in database
     * @param newMap - map object
     * @param dungeonEntity - dungeon object
     * @param monsterEntity - monster object (can be null if there is no monster)
     * @param monsterItem - monster item
     * @param secretItem - secret item
     * @param x - location x in map
     * @param y - location y in map
     */
    private void addMapDungeon(MapEntity newMap, DungeonEntity dungeonEntity, MonsterEntity monsterEntity, ItemEntity monsterItem, ItemEntity secretItem, Integer x, Integer y) {
        MapDungeonEntity newMappedDungeon = new MapDungeonEntity(newMap, dungeonEntity, monsterEntity, monsterItem, secretItem, x, y);
        mapDungeonRepository.add(newMappedDungeon);
    }

    /**
     * Gets dungeons located in map from database
     * @param mapId - id of map
     * @return list of MapDungeonEntity object
     */
    private List<MapDungeonEntity> getMappedDungeons(Integer mapId){
        return mapDungeonRepository.getMapDungeons(mapId);
    }

    /**
     * Gets map from database
     * @param mapId - id of map
     * @return MapEntity object
     */
    @Override
    public MapEntity getMap(Integer mapId){
        return mapRepository.findById(mapId);
    }

    /**
     * Gets current player dungeon from database
     * @param mapId - map id
     * @param playerX - player x coordinate on map
     * @param playerY - player y coordinate on map
     * @return MapDungeonEntity object
     */
    @Override
    public MapDungeonEntity getCurrentPlayerDungeonLocation(Integer mapId, Integer playerX, Integer playerY) {
        return mapDungeonRepository.getCurrentPlayerDungeonLocation(mapId, playerX, playerY);
    }

    /**
     * Creates action messages based on players location on map
     * @param map - map object
     * @return List of action messages
     */
    @Override
    public List<String> createActions(MapEntity map){
        List<String> result = new ArrayList<>();
        MapDungeonEntity currentDungeon = this.getCurrentPlayerDungeonLocation(map.getId(), map.getPlayerLocationX(), map.getPlayerLocationY());
        boolean isPlayerNextToBossCave = (map.getPlayerLocationX() == map.getMapDimensionX() - 1 && map.getPlayerLocationY() == 1) || (map.getPlayerLocationX() == map.getMapDimensionX() - 2 && map.getPlayerLocationY() == 2);
        boolean canPlayerMoveToBoss = (map.isPlayerHasKey() && isPlayerNextToBossCave);
        boolean canPlayerMoveFromMonsterDungeon = (currentDungeon.getMonster() == null || currentDungeon.isMonsterFriend());
        if(map.getPlayerLocationX() == 0 && map.getPlayerLocationY() == 0){
            result.add("Shop");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationX() > 0){
            result.add("Move up");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationX() < map.getMapDimensionX() - 1 && !isPlayerNextToBossCave){
            result.add("Move down");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationY() > 0){
            result.add("Move left");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationY() < map.getMapDimensionY() - 1 && !isPlayerNextToBossCave){
            result.add("Move right");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationY() < map.getMapDimensionY() - 1 && canPlayerMoveToBoss){
            result.add("Move right");
        }
        if(canPlayerMoveFromMonsterDungeon && map.getPlayerLocationX() < map.getMapDimensionX() - 1 && canPlayerMoveToBoss){
            result.add("Move down");
        }
        if(currentDungeon.getMonster() != null && currentDungeon.getMonsterHP() <= 100 && !currentDungeon.isMonsterFriend()){
            result.add("Fight Monster");
        }
        if(currentDungeon.getMonster() != null && currentDungeon.getMonsterHP() == 100 && !currentDungeon.isMonsterFriend()){
            result.add("Befriend Monster");
        }
        if(currentDungeon.getMonster() != null && !currentDungeon.isMonsterFriend()){
            result.add("Flee");
        }
        if(currentDungeon.getMonster() == null || currentDungeon.isMonsterFriend()){
            result.add("Search for secret items");
        }

        return result;
    }

    /**
     * Finds dungeon where player is located
     * @param playerX - player current X coordinate
     * @param playerY - player current Y coordinate
     * @param dungeons - list of dungeons
     * @return MapDungeonEntity object
     */
    private MapDungeonEntity getCurrentDungeonPlayerLocation(Integer playerX, Integer playerY, List<MapDungeonEntity> dungeons){
        MapDungeonEntity result = null;
        for(MapDungeonEntity dungeon : dungeons){
            if(dungeon.getLocationX().equals(playerX) && dungeon.getLocationY().equals(playerY)){
                result = dungeon;
                break;
            }
        }

        return result;
    }

    /**
     * Handles search item action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param hero - HeroEntity object
     * @return message
     */
    private String actionSearchSecretItem(MapDungeonEntity currentDungeon, MapEntity currentMap, HeroEntity hero) {
        String message = "";
        if(currentDungeon.getDungeon().getId() == 1 || currentDungeon.getDungeon().getId() == 2){
            message = "You can't search for items here";
        }
        else if(!currentDungeon.isMonsterFriend()) {
            message = "You need to handle monster first";
        }
        else if(currentDungeon.getSecretItem() != null && currentDungeon.isMonsterFriend()){
            inventoryService.addHeroItemToInventory(hero.getId(), currentDungeon.getSecretItem().getId());
            message = "You found " + currentDungeon.getSecretItem().getName() + ". Nice!";
            currentDungeon.setSecretItem(null);
            if(hero.getHealth() > 10){
                hero.setHealth(hero.getHealth() - 10);
                message = message + " You lost 10HP for the effort to find the item";
            }
        }else{
            message = "No item found";
            if(hero.getHealth() > 10){
                hero.setHealth(hero.getHealth() - 10);
                message = "No item found, and you lost 10HP for the effort to find the item";
            }
        }

        mapDungeonRepository.save(currentDungeon);
        heroService.saveHero(hero);
        return message;
    }

    /**
     * Handles fight action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param hero - HeroEntity object
     * @return message
     */
    private String actionFight(MapDungeonEntity currentDungeon, MapEntity currentMap, HeroEntity hero){
        String message = "";
        if(currentDungeon.getMonster() == null || currentDungeon.isMonsterFriend()){
            message =  "Invalid action";
        }else{
            while(hero.getHealth() > 0 && currentDungeon.getMonsterHP() > 0){
                if(hero.getWeapon() != null){
                    currentDungeon.setMonsterHP(currentDungeon.getMonsterHP() - (hero.getDamage() * hero.getWeapon().getDamage()) / (new Random().nextInt(4) + 1));
                } else {
                    currentDungeon.setMonsterHP(currentDungeon.getMonsterHP() - (hero.getDamage()  / (new Random().nextInt(4) + 1)));
                }

                if(currentDungeon.getMonsterHP() > 0){
                    hero.setHealth(hero.getHealth() - (currentDungeon.getMonster().getDamage() / (new Random().nextInt(4) + 1)));
                }
            }

            if(hero.getHealth() <= 0){
                message = "You died! " + currentDungeon.getMonster().getName() + " has ended your career";
            }

            mapDungeonRepository.save(currentDungeon);
            if(currentDungeon.getMonsterHP() <= 0){
                if(currentDungeon.getMonsterItem().getName().equals("Key")){
                    currentMap.setPlayerHasKey(true);
                    mapRepository.save(currentMap);
                }else{
                    inventoryService.addHeroItemToInventory(hero.getId(), currentDungeon.getMonsterItem().getId());
                }
                currentDungeon.setMonsterFriend(true);
                hero.setCoins(hero.getCoins() + currentDungeon.getMonster().getDamage());
                hero.setDamage(hero.getDamage() - new Random().nextInt(15));
                message = "Well done adventurer, you defeated " + currentDungeon.getMonster().getName() + " and got " + currentDungeon.getMonsterItem().getName() + " from him. Your current health is " + hero.getHealth();
                currentDungeon.setMonsterItem(null);
                currentDungeon.setMonster(null);
                mapDungeonRepository.save(currentDungeon);
            }
        }

        heroService.saveHero(hero);
        return message;
    }

    /**
     * Handles befriend action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param hero - HeroEntity object
     * @return message
     */
    private String actionBefriend(MapDungeonEntity currentDungeon, MapEntity currentMap, HeroEntity hero) {
        String message = "";
        if(currentDungeon.getMonster() == null){
            message = "No monster in this dungeon!";
        }
        else if(currentDungeon.getMonster() != null && currentDungeon.isMonsterFriend()){
            message = "Monster is already a friend";
        }
        else if(currentDungeon.getMonster().getName().equals("The Orb Boss")){
            message = "You can't be friends with the Boss monster! Fight him!";
        }
        else{
            if(new Random().nextInt(100) > 70){
                currentDungeon.setMonsterFriend(true);
                message = "You ask " + currentDungeon.getMonster().getName() + " to be you friend and he accepts. He gave you " + currentDungeon.getMonsterItem().getName();
                if(currentDungeon.getMonsterItem().getName().equals("Key")){
                    currentMap.setPlayerHasKey(true);
                    mapRepository.save(currentMap);
                }else{
                    inventoryService.addHeroItemToInventory(hero.getId(), currentDungeon.getMonsterItem().getId());
                }
                currentDungeon.setMonsterItem(null);
            } else{
                return this.actionFight(currentDungeon, currentMap, hero);
            }
        }

        mapDungeonRepository.save(currentDungeon);
        return message;
    }

    /**
     * Handles flee action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param hero - HeroEntity object
     * @return message object
     */
    private String actionFlee(MapDungeonEntity currentDungeon, MapEntity currentMap, HeroEntity hero) {
        String message = "";
        if(currentDungeon.getMonster() == null || currentDungeon.isMonsterFriend()){
            message = "Invalid action! No monster to fight";
        }else{
            currentMap.setPlayerLocationX(0);
            currentMap.setPlayerLocationY(0);
            message = "You escaped to the lobby.";
            if(hero.getHealth() > 15){
                hero.setHealth(hero.getHealth() - 15);
                message = message + " It cost you 15HP";
                heroService.saveHero(hero);
            }
        }

        mapRepository.save(currentMap);
        return message;
    }
}
