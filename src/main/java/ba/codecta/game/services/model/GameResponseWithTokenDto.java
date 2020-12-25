package ba.codecta.game.services.model;

import java.util.List;

public class GameResponseWithTokenDto {
    private String message;
    private HeroDto hero;
    private List<String> actions;
    private MapDungeonDto currentDungeon;
    private InventoryDto inventory;
    private String token;

    public GameResponseWithTokenDto() {
    }

    public GameResponseWithTokenDto(String message, HeroDto hero, List<String> actions, MapDungeonDto currentDungeon, InventoryDto inventory, String token) {
        this.message = message;
        this.hero = hero;
        this.actions = actions;
        this.currentDungeon = currentDungeon;
        this.inventory = inventory;
        this.token = token;
    }

    public HeroDto getHero() {
        return hero;
    }

    public void setHero(HeroDto hero) {
        this.hero = hero;
    }

    public InventoryDto getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDto inventory) {
        this.inventory = inventory;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
