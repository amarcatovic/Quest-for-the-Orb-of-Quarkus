package ba.codecta.game.services;

import ba.codecta.game.repository.entity.ItemEntity;
import ba.codecta.game.services.model.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemEntity> getAllItems();
}
