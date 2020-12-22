package ba.codecta.game.services.model;

import ba.codecta.game.repository.entity.MapDungeonEntity;

import java.util.List;

public class MapDto {
    private Integer id;
    private String message;
    private List<String> actions;
    private Integer playerLocationX;
    private Integer playerLocationY;
    private boolean playerHasKey;
    private List<MapDungeonDto> dungeons;
    private MapDungeonDto currentDungeon;
    private Integer mapDimensionX;
    private Integer mapDimensionY;

    public Integer getPlayerLocationX() {
        return playerLocationX;
    }

    public void setPlayerLocationX(Integer playerLocationX) {
        this.playerLocationX = playerLocationX;
    }

    public Integer getPlayerLocationY() {
        return playerLocationY;
    }

    public void setPlayerLocationY(Integer playerLocationY) {
        this.playerLocationY = playerLocationY;
    }

    public Integer getMapDimensionX() {
        return mapDimensionX;
    }

    public void setMapDimensionX(Integer mapDimensionX) {
        this.mapDimensionX = mapDimensionX;
    }

    public Integer getMapDimensionY() {
        return mapDimensionY;
    }

    public void setMapDimensionY(Integer mapDimensionY) {
        this.mapDimensionY = mapDimensionY;
    }

    public MapDto() {
    }

    public MapDto(Integer id, String message, List<String> actions, Integer playerLocationX, Integer playerLocationY, boolean playerHasKey, Integer x, Integer y, MapDungeonDto currentDungeon) {
        this.id = id;
        this.message = message;
        this.actions = actions;
        this.playerLocationX = playerLocationX;
        this.playerLocationY = playerLocationY;
        this.playerHasKey = playerHasKey;
        this.mapDimensionX = x;
        this.mapDimensionY = y;
        this.currentDungeon = currentDungeon;
    }

    public boolean isPlayerHasKey() {
        return playerHasKey;
    }

    public void setPlayerHasKey(boolean playerHasKey) {
        this.playerHasKey = playerHasKey;
    }

    public List<MapDungeonDto> getDungeons() {
        return dungeons;
    }

    public void setDungeons(List<MapDungeonDto> dungeons) {
        this.dungeons = dungeons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public MapDungeonDto getCurrentDungeon() {
        return currentDungeon;
    }

    public void setCurrentDungeon(MapDungeonDto currentDungeon) {
        this.currentDungeon = currentDungeon;
    }
}
