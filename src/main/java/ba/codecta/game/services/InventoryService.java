package ba.codecta.game.services;

import ba.codecta.game.repository.entity.InventoryEntity;
import ba.codecta.game.services.model.InventoryDto;

public interface InventoryService {
    InventoryEntity addHeroItemToInventory(Integer heroId, Integer ItemId);
    InventoryDto getHeroInventory(Integer heroId);
    void removeItemFromInventory(Integer heroId, Integer itemId);
}
