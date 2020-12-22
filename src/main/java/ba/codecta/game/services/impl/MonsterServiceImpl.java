package ba.codecta.game.services.impl;

import ba.codecta.game.repository.MonsterRepository;
import ba.codecta.game.repository.entity.MonsterEntity;
import ba.codecta.game.services.MonsterService;
import ba.codecta.game.services.model.MonsterDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class MonsterServiceImpl implements MonsterService {

    @Inject
    MonsterRepository monsterRepository;

    @Override
    public List<MonsterEntity> getAllMonsters() {
        List<MonsterEntity> monstersList = monsterRepository.findAll();
        if(monstersList == null || monstersList.isEmpty()) {
            return null;
        }

        return monstersList;
    }
}
