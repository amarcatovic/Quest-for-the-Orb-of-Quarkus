package ba.codecta.game.services.impl;

import ba.codecta.game.repository.LevelRepository;
import ba.codecta.game.repository.entity.LevelEntity;
import ba.codecta.game.repository.entity.MapEntity;
import ba.codecta.game.services.LevelService;
import ba.codecta.game.services.MapService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LevelServiceImpl implements LevelService {

    @Inject
    LevelRepository levelRepository;

    @Inject
    MapService mapService;

    @Override
    public LevelEntity createLevel(Integer levelWeight, Integer mapId) {
        LevelEntity newLevel = new LevelEntity();
        newLevel.setMap(mapService.getMap(mapId));
        newLevel.setWeightFactor(levelWeight);
        return levelRepository.add(newLevel);
    }

    @Override
    public LevelEntity getLevelEntity(Integer levelId) {
        return levelRepository.findById(levelId);
    }
}
