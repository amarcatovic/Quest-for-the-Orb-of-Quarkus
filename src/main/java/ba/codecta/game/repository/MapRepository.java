package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.MapEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class MapRepository extends Repository<MapEntity, Integer> {

    public MapRepository() {
        super(MapEntity.class);
    }
}
