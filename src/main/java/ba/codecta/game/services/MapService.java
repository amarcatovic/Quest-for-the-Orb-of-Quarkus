package ba.codecta.game.services;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.repository.entity.MapEntity;
import ba.codecta.game.services.model.MapDto;

public interface MapService {
    MapDto createMap(Integer levelWeightFactor);
    MapDto move(Integer mapId, MoveDirection moveDirection);
    MapDto action(Integer heroId, Integer mapId, MapAction mapAction);
    MapDto getStatus(Integer mapId, String message);
    MapEntity getMap(Integer mapId);
}
