package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class UserRepository extends Repository<UserEntity, Integer> {
    public UserRepository() {
        super(UserEntity.class);
    }

    public List<UserEntity> findUserByEmail(String email){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> root = cq.from(UserEntity.class);
        CriteriaQuery<UserEntity> all = cq.select(root);
        all.where(cb.equal(root.get("email"), email));
        TypedQuery<UserEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
}
