package ba.codecta.game.services.impl;

import ba.codecta.game.repository.ItemRepository;
import ba.codecta.game.repository.entity.ItemEntity;
import ba.codecta.game.services.ItemService;
import ba.codecta.game.services.model.HeroDto;
import ba.codecta.game.services.model.ItemDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class ItemServiceImpl implements ItemService {
    @Inject
    ItemRepository itemRepository;

    @Override
    public List<ItemEntity> getAllItems() {
        List<ItemEntity> itemsList = itemRepository.findAll();
        if(itemsList == null || itemsList.isEmpty()) {
            return null;
        }
        return itemsList;
    }

    @Override
    public ItemEntity getItemById(Integer id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<ItemDto> getAllHealingItemsForShop() {
        List<ItemEntity> itemsList = itemRepository.getAllHealingItemsForShop();
        if(itemsList == null || itemsList.size() == 0){
            return null;
        }
        List<ItemDto> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for(ItemEntity item : itemsList){
           result.add(modelMapper.map(item, ItemDto.class));
        }
        return result;
    }

    @Override
    public List<ItemDto> getAllStrengthItemsForShop() {
        List<ItemEntity> itemsList = itemRepository.getAllStrengthItemsForShop();
        if(itemsList == null || itemsList.size() == 0){
            return null;
        }
        List<ItemDto> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for(ItemEntity item : itemsList){
            result.add(modelMapper.map(item, ItemDto.class));
        }
        return result;
    }
}
