package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.LevelEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class LevelRepository extends Repository<LevelEntity, Integer> {
    public LevelRepository() {
        super(LevelEntity.class);
    }
}
