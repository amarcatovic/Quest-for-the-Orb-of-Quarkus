package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.HeroEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class HeroRepository extends Repository<HeroEntity, Integer> {

    public HeroRepository() {
        super(HeroEntity.class);
    }
}
