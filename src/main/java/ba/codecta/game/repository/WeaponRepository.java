package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.WeaponEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class WeaponRepository extends Repository<WeaponEntity, Integer> {
    public WeaponRepository() {
        super(WeaponEntity.class);
    }
}
