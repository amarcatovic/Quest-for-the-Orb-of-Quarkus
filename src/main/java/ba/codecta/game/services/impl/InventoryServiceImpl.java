package ba.codecta.game.services.impl;

import ba.codecta.game.repository.HeroRepository;
import ba.codecta.game.repository.InventoryRepository;
import ba.codecta.game.repository.ItemRepository;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.repository.entity.InventoryEntity;
import ba.codecta.game.repository.entity.ItemEntity;
import ba.codecta.game.services.HeroService;
import ba.codecta.game.services.model.InventoryDto;
import ba.codecta.game.services.InventoryService;
import ba.codecta.game.services.model.ItemDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class InventoryServiceImpl implements InventoryService {

    @Inject
    InventoryRepository inventoryRepository;

    @Inject
    HeroRepository heroRepository;

    @Inject
    ItemRepository itemRepository;

    @Inject
    HeroService heroService;

    @Override
    public InventoryEntity addHeroItemToInventory(Integer heroId, Integer itemId) {
        HeroEntity heroFromDb = heroRepository.findById(heroId);
        ItemEntity itemFromDb = itemRepository.findById(itemId);
        InventoryEntity inventory = new InventoryEntity(heroFromDb, itemFromDb);
        return inventoryRepository.add(inventory);
    }

    @Override
    public InventoryDto getHeroInventory(Integer heroId) {
        HeroEntity heroFromDb = heroRepository.findById(heroId);
        List<InventoryEntity> inventoryItems = inventoryRepository.getAllHeroItemsFromInventory(heroId);
        InventoryDto inventoryDto = new InventoryDto(heroFromDb.getName() + "'s items");
        ModelMapper modelMapper = new ModelMapper();
        for(InventoryEntity inventoryItem : inventoryItems){
            inventoryDto.addItemToList(modelMapper.map(inventoryItem.getItem(), ItemDto.class));
        }

        return inventoryDto;
    }

    @Override
    public void removeItemFromInventory(Integer heroId, Integer itemId) {
        HeroEntity heroFromDb = heroRepository.findById(heroId);
        ItemEntity itemFromDb = itemRepository.findById(itemId);
        InventoryEntity inventoryItem = inventoryRepository.getHeroItemFromInventory(heroId, itemId);
        if(itemFromDb.getId() == 1){
            heroService.increaseHealth(heroFromDb, itemFromDb.getBonus());
        } else if(itemFromDb.getId() == 2){
            heroService.increaseDamage(heroFromDb, itemFromDb.getBonus());
        }

        inventoryRepository.delete(inventoryItem);
    }
}
