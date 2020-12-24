package ba.codecta.game.services.model;

import java.util.List;

public class GameResponseDto {
    private String message;
    private HeroDto hero;
    private List<String> actions;
    private MapDungeonDto currentDungeon;
    private InventoryDto inventory;

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
}
