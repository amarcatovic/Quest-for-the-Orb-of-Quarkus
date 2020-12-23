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
                if(new Random().nextInt(100) > 40){
                    monster = monsters.get(new Random().nextInt(monsters.size() - 1) + 1);
                    monsterItem = items.get(new Random().nextInt(items.size() - 2) + 2);
                    if(!isKeyGivenToMonster){
                        if(new Random().nextInt() > 40){
                            monsterItem = items.get(1);
                            isKeyGivenToMonster = true;
                        }
                    }
                }

                ItemEntity secretItem = items.get(new Random().nextInt(items.size()));
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
     * @return MapDto object
     */
    @Override
    public MapDto move(Integer mapId, MoveDirection moveDirection) {
        MapEntity currentMap = this.getMap(mapId);
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);

        if((moveDirection == MoveDirection.UP && currentMap.getPlayerLocationX() == 0) || (moveDirection == MoveDirection.DOWN && currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1) ||
                (moveDirection == MoveDirection.LEFT && currentMap.getPlayerLocationY() == 0) || (moveDirection == MoveDirection.RIGHT && currentMap.getPlayerLocationY() == currentMap.getMapDimensionY() - 1)){
            return this.createMapDto(currentMap.getId(), "Invalid move! You hit the wall", this.createActions(currentMap, mappedDungeons));
        }
        if(!currentDungeon.isMonsterFriend() || currentDungeon.getMonsterHP() > 0){
            return this.createMapDto(currentMap.getId(), "You need to interact with the monster first", this.createActions(currentMap, mappedDungeons));
        }
        if(!currentMap.isPlayerHasKey() && ((currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1 && currentMap.getPlayerLocationY() == 1) || (currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 2 && currentMap.getPlayerLocationY() == 2))){
            return this.createMapDto(currentMap.getId(), "You don't have the key", this.createActions(currentMap, mappedDungeons));
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
        return this.createMapDto(currentMap.getId(), message, this.createActions(currentMap, mappedDungeons));
    }

    /**
     * Handles player action
     * @param heroId - id of hero
     * @param mapId - map id
     * @param mapAction - action
     * @return MapDto Object
     */
    @Override
    public MapDto action(Integer heroId, Integer mapId, MapAction mapAction) {
        MapEntity currentMap = this.getMap(mapId);
        HeroEntity hero = heroService.getHeroEntityById(heroId);
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);

        if((mapAction == MapAction.BEFRIEND && currentDungeon.getMonster() == null) || (mapAction == MapAction.FIGHT && currentDungeon.getMonster() == null)
        || (mapAction == MapAction.FLEE && currentDungeon.getMonster() == null)){
            return this.createMapDto(currentMap.getId(), "Invalid action! There is no monster in this dungeon!", this.createActions(currentMap, mappedDungeons));
        }

        // FIGHT
        if(mapAction == MapAction.FIGHT){
            return this.actionFight(currentDungeon, currentMap, mappedDungeons, hero);
        }

        // SEARCH SECRET ITEM
        if(mapAction == MapAction.SEARCH_SECRET_ITEM){
            return this.actionSearchSecretItem(currentDungeon, currentMap, mappedDungeons, hero);
        }

        // BEFRIEND MONSTER
        if(mapAction == MapAction.BEFRIEND){
            return this.actionBefriend(currentDungeon, currentMap, mappedDungeons, hero);
        }

        // FLEE
        if(mapAction == MapAction.FLEE){
            return this.actionFlee(currentDungeon, currentMap, mappedDungeons, hero);
        }


        return new MapDto();
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
        MapDungeonEntity newMapedDungeon = new MapDungeonEntity(newMap, dungeonEntity, monsterEntity, monsterItem, secretItem, x, y);
        mapDungeonRepository.add(newMapedDungeon);
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
    private MapEntity getMap(Integer mapId){
        return mapRepository.findById(mapId);
    }

    /**
     * Creates action messages based on players location on map
     * @param map - map object
     * @param dungeons - list of dungeons located in map
     * @return List of action messages
     */
    private List<String> createActions(MapEntity map, List<MapDungeonEntity> dungeons){
        List<String> result = new ArrayList<>();
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(map.getPlayerLocationX(), map.getPlayerLocationY(), dungeons);
        boolean isPlayerNextToBossCave = (map.getPlayerLocationX() == map.getMapDimensionX() - 1 && map.getPlayerLocationY() == 1) || (map.getPlayerLocationX() == map.getMapDimensionX() - 2 && map.getPlayerLocationY() == 2);
        boolean canPlayerMoveToBoss = (map.isPlayerHasKey() && isPlayerNextToBossCave);
        boolean canPlayerMoveFromMonsterDungeon = (currentDungeon.getMonster() == null && currentDungeon.isMonsterFriend());
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
     * @param mappedDungeons - List of MapDungeonEntity objects
     * @param hero - HeroEntity object
     * @return MapDto object
     */
    private MapDto actionSearchSecretItem(MapDungeonEntity currentDungeon, MapEntity currentMap, List<MapDungeonEntity> mappedDungeons, HeroEntity hero) {
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
        return this.createMapDto(currentMap.getId(), message, this.createActions(currentMap, mappedDungeons));
    }

    /**
     * Handles fight action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param mappedDungeons - List of MapDungeonEntity objects
     * @param hero - HeroEntity object
     * @return MapDto object
     */
    private MapDto actionFight(MapDungeonEntity currentDungeon, MapEntity currentMap, List<MapDungeonEntity> mappedDungeons, HeroEntity hero){
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
                message = "Well done adventurer, you defeated " + currentDungeon.getMonster().getName() + " and got " + currentDungeon.getMonsterItem().getName() + " from him. Your current health is " + hero.getHealth();
                currentDungeon.setMonsterItem(null);
                currentDungeon.setMonster(null);
                mapDungeonRepository.save(currentDungeon);
            }
        }

        heroService.saveHero(hero);
        return this.createMapDto(currentMap.getId(), message, this.createActions(currentMap, mappedDungeons));
    }

    /**
     * Handles befriend action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param mappedDungeons - List of MapDungeonEntity objects
     * @param hero - HeroEntity object
     * @return MapDto object
     */
    private MapDto actionBefriend(MapDungeonEntity currentDungeon, MapEntity currentMap, List<MapDungeonEntity> mappedDungeons, HeroEntity hero) {
        String message = "";
        if(currentDungeon.getMonster() == null){
            message = "No monster in this dungeon!";
        }
        else if(currentDungeon.getMonster() != null && currentDungeon.isMonsterFriend()){
            message = "Monster is already a friend";
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
                return this.actionFight(currentDungeon, currentMap, mappedDungeons, hero);
            }
        }

        mapDungeonRepository.save(currentDungeon);
        return this.createMapDto(currentMap.getId(), message, this.createActions(currentMap, mappedDungeons));
    }

    /**
     * Handles flee action
     * @param currentDungeon - MapDungeonEntity object
     * @param currentMap - MapEntity object
     * @param mappedDungeons - List of MapDungeonEntity objects
     * @param hero - HeroEntity object
     * @return MapDto object
     */
    private MapDto actionFlee(MapDungeonEntity currentDungeon, MapEntity currentMap, List<MapDungeonEntity> mappedDungeons, HeroEntity hero) {
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
        return this.createMapDto(currentMap.getId(), message, this.createActions(currentMap, mappedDungeons));
    }
}
