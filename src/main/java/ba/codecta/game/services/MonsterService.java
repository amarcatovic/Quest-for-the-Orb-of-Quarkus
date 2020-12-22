package ba.codecta.game.services;

import ba.codecta.game.repository.entity.MonsterEntity;
import ba.codecta.game.services.model.MonsterDto;

import java.util.List;

public interface MonsterService {
    List<MonsterEntity> getAllMonsters();
}
