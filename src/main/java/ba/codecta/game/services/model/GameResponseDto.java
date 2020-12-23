package ba.codecta.game.services.model;

import java.util.List;

public class GameResponseDto {
    private Integer gameId;
    private HeroDto hero;
    private MapDto mapInfo;
    private InventoryDto inventory;

    public HeroDto getHero() {
        return hero;
    }

    public void setHero(HeroDto hero) {
        this.hero = hero;
    }

    public MapDto getMapInfo() {
        return mapInfo;
    }

    public void setMapInfo(MapDto mapInfo) {
        this.mapInfo = mapInfo;
    }

    public InventoryDto getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDto inventory) {
        this.inventory = inventory;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
