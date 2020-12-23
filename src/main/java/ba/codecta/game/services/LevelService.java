package ba.codecta.game.services;

import ba.codecta.game.repository.entity.LevelEntity;
import ba.codecta.game.repository.entity.MapEntity;

public interface LevelService {
    LevelEntity createLevel(Integer levelWeight, Integer mapId);
    LevelEntity getLevelEntity(Integer levelId);
}
