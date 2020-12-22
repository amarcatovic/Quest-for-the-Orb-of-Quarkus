package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.*;
import ba.codecta.game.repository.entity.*;
import ba.codecta.game.services.DungeonService;
import ba.codecta.game.services.ItemService;
import ba.codecta.game.services.MapService;
import ba.codecta.game.services.MonsterService;
import ba.codecta.game.services.model.MapDto;
import ba.codecta.game.services.model.MapDungeonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.rule.Mode;
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

    @Override
    public MapDto createMap(Integer levelWeightFactor) {
        MapEntity newMap = new MapEntity(2 + levelWeightFactor, 3);
        newMap = mapRepository.add(newMap);

        List<DungeonEntity> dungeons = dungeonService.getAllDungeons();
        List<MonsterEntity> monsters = monsterService.getAllMonsters();
        List<ItemEntity> items = itemService.getAllItems();

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
                    monsterItem = items.get(new Random().nextInt(items.size()));
                }

                ItemEntity secretItem = items.get(new Random().nextInt(items.size()));
                this.addMapDungeon(newMap, dungeon, monster, monsterItem, secretItem,i, j);
            }
        }
        this.addMapDungeon(newMap, dungeons.get(1), monsters.get(0), null, null,1 + levelWeightFactor, 2);

        List<String> actions = Arrays.asList("Move Down", "Move right", "Shop");
        return this.createMapDto(newMap.getId(),"Game has started, good luck on your quest adventurer!", actions);
    }

    @Override
    public MapDto move(Integer mapId, MoveDirection moveDirection) {
        MapEntity currentMap = this.getMap(mapId);
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);

        if((moveDirection == MoveDirection.UP && currentMap.getPlayerLocationX() == 0) || (moveDirection == MoveDirection.DOWN && currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1) ||
                (moveDirection == MoveDirection.LEFT && currentMap.getPlayerLocationY() == 0) || (moveDirection == MoveDirection.RIGHT && currentMap.getPlayerLocationY() == currentMap.getMapDimensionY() - 1)){
            return this.createMapDto(currentMap.getId(), "Invalid move! You hit the wall", this.createActions(currentMap, mappedDungeons));
        }
        if(!currentDungeon.isMonsterFriend()){
            return this.createMapDto(currentMap.getId(), "You need to interact with the monster first", this.createActions(currentMap, mappedDungeons));
        }
        if(!currentMap.isPlayerHasKey() && ((currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 1 && currentMap.getPlayerLocationY() == 1) || (currentMap.getPlayerLocationX() == currentMap.getMapDimensionX() - 2 && currentMap.getPlayerLocationY() == 2))){
            return this.createMapDto(currentMap.getId(), "You don't have the key", this.createActions(currentMap, mappedDungeons));
        }

        if(moveDirection == MoveDirection.UP){
            currentMap.setPlayerLocationX(currentMap.getPlayerLocationX() - 1);
            mapRepository.save(currentMap);
            return this.createMapDto(currentMap.getId(), "You moved up", this.createActions(currentMap, mappedDungeons));
        }
        if(moveDirection == MoveDirection.DOWN){
            currentMap.setPlayerLocationX(currentMap.getPlayerLocationX() + 1);
            mapRepository.save(currentMap);
            return this.createMapDto(currentMap.getId(), "You moved down", this.createActions(currentMap, mappedDungeons));
        }
        if(moveDirection == MoveDirection.RIGHT){
            currentMap.setPlayerLocationY(currentMap.getPlayerLocationY() + 1);
            mapRepository.save(currentMap);
            return this.createMapDto(currentMap.getId(), "You moved right", this.createActions(currentMap, mappedDungeons));
        }
        if(moveDirection == MoveDirection.LEFT){
            currentMap.setPlayerLocationY(currentMap.getPlayerLocationY() - 1);
            mapRepository.save(currentMap);
            return this.createMapDto(currentMap.getId(), "You moved left", this.createActions(currentMap, mappedDungeons));
        }

        return null;
    }

    @Override
    public MapDto action(Integer mapId, MapAction mapAction) {
        MapEntity currentMap = this.getMap(mapId);
        List<MapDungeonEntity> mappedDungeons = this.getMappedDungeons(mapId);
        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(currentMap.getPlayerLocationX(), currentMap.getPlayerLocationY(), mappedDungeons);

        if((mapAction == MapAction.BEFRIEND && currentDungeon.getMonster() == null) || (mapAction == MapAction.FIGHT && currentDungeon.getMonster() == null)
        || (mapAction == MapAction.FLEE && currentDungeon.getMonster() == null)){
            return this.createMapDto(currentMap.getId(), "Invalid action! There is no monster in this dungeon!", this.createActions(currentMap, mappedDungeons));
        }

        if(mapAction == MapAction.FIGHT){
            
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
        boolean canPlayerMoveToBoss = (map.isPlayerHasKey() && (map.getPlayerLocationX() == map.getMapDimensionX() - 1 && map.getPlayerLocationY() == 1) && (map.getPlayerLocationX() == map.getMapDimensionX() - 2 && map.getPlayerLocationY() == 2));
        if(map.getPlayerLocationX() == 0 && map.getPlayerLocationY() == 0){
            result.add("Shop");
        }
        if(map.getPlayerLocationX() > 0){
            result.add("Move up");
        }
        if(map.getPlayerLocationX() < map.getMapDimensionX() - 1 && canPlayerMoveToBoss){
            result.add("Move down");
        }
        if(map.getPlayerLocationY() > 0){
            result.add("Move left");
        }
        if(map.getPlayerLocationY() < map.getMapDimensionY() - 1 && canPlayerMoveToBoss){
            result.add("Move right");
        }

        MapDungeonEntity currentDungeon = this.getCurrentDungeonPlayerLocation(map.getPlayerLocationX(), map.getPlayerLocationY(), dungeons);
        if(currentDungeon.getMonster() != null && currentDungeon.getMonsterHP() < 100 && !currentDungeon.isMonsterFriend()){
            result.add("Fight Monster");
        }
        if(currentDungeon.getMonster() != null && currentDungeon.getMonsterHP() == 100 && currentDungeon.isMonsterFriend()){
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
        for(MapDungeonEntity dungeon : dungeons){
            if(dungeon.getLocationX().equals(playerX) && dungeon.getLocationY().equals(playerY)){
                return dungeon;
            }
        }

        return null;
    }
}
