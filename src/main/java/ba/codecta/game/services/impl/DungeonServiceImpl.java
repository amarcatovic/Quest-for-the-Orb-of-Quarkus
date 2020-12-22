package ba.codecta.game.services.impl;

import ba.codecta.game.repository.DungeonRepository;
import ba.codecta.game.repository.entity.DungeonEntity;
import ba.codecta.game.repository.entity.ItemEntity;
import ba.codecta.game.services.DungeonService;
import ba.codecta.game.services.model.DungeonDto;
import ba.codecta.game.services.model.ItemDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class DungeonServiceImpl implements DungeonService {

    @Inject
    DungeonRepository dungeonRepository;

    @Override
    public List<DungeonEntity> getAllDungeons() {
        List<DungeonEntity> dungeonList = dungeonRepository.findAll();
        if(dungeonList == null || dungeonList.isEmpty()) {
            return null;
        }

        return dungeonList;
    }
}
