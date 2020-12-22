package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.MonsterEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class MonsterRepository extends Repository<MonsterEntity, Integer> {

    public MonsterRepository() {
        super(MonsterEntity.class);
    }
}
