package ba.codecta.game.services;

import ba.codecta.game.repository.entity.DungeonEntity;
import ba.codecta.game.services.model.DungeonDto;

import java.util.List;

public interface DungeonService {
    List<DungeonEntity> getAllDungeons();
}
