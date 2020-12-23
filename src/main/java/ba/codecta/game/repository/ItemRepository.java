package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.GameEntity;
import ba.codecta.game.repository.entity.ItemEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class ItemRepository extends Repository<ItemEntity, Integer> {

    public ItemRepository() {
        super(ItemEntity.class);
    }

    public List<ItemEntity> getAllHealingItemsForShop(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemEntity> cq = cb.createQuery(ItemEntity.class);
        Root<ItemEntity> root = cq.from(ItemEntity.class);
        CriteriaQuery<ItemEntity> all = cq.select(root);
        all.where(cb.equal(root.get("itemType"), 1));
        TypedQuery<ItemEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public List<ItemEntity> getAllStrengthItemsForShop(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemEntity> cq = cb.createQuery(ItemEntity.class);
        Root<ItemEntity> root = cq.from(ItemEntity.class);
        CriteriaQuery<ItemEntity> all = cq.select(root);
        all.where(cb.equal(root.get("itemType"), 2));
        TypedQuery<ItemEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
}
