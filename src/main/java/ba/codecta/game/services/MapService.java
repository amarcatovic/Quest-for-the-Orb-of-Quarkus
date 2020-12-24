package ba.codecta.game.services;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.repository.entity.MapDungeonEntity;
import ba.codecta.game.repository.entity.MapEntity;
import ba.codecta.game.services.model.MapDto;

import java.util.List;

public interface MapService {
    MapDto createMap(Integer levelWeightFactor);
    String move(Integer mapId, MoveDirection moveDirection);
    String action(Integer heroId, Integer mapId, MapAction mapAction);
    MapDto getStatus(Integer mapId, String message);
    MapEntity getMap(Integer mapId);
    List<String> createActions(MapEntity map);
    MapDungeonEntity getCurrentPlayerDungeonLocation(Integer mapId, Integer playerX, Integer playerY);
}
