package ba.codecta.game.services.model;

import java.util.List;

public class GameCreateResponseDto {
    private String message;
    private HeroDto hero;
    private List<String> actions;
    private MapDungeonDto currentDungeon;
    private InventoryDto inventory;
    private List<MapDungeonDto> dungeons;
    private String token;

    public HeroDto getHero() {
        return hero;
    }

    public void setHero(HeroDto hero) {
        this.hero = hero;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MapDungeonDto> getDungeons() {
        return dungeons;
    }

    public void setDungeons(List<MapDungeonDto> dungeons) {
        this.dungeons = dungeons;
    }

    public MapDungeonDto getCurrentDungeon() {
        return currentDungeon;
    }

    public void setCurrentDungeon(MapDungeonDto currentDungeon) {
        this.currentDungeon = currentDungeon;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public InventoryDto getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDto inventory) {
        this.inventory = inventory;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
