package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.DungeonEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class DungeonRepository extends Repository<DungeonEntity, Integer> {
    public DungeonRepository() {
        super(DungeonEntity.class);
    }
}
