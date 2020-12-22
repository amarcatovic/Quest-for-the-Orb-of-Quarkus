package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.InventoryEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class InventoryRepository extends Repository<InventoryEntity, Integer> {

    public InventoryRepository() {
        super(InventoryEntity.class);
    }

    public List<InventoryEntity> getAllHeroItemsFromInventory(Integer heroId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InventoryEntity> cq = cb.createQuery(InventoryEntity.class);
        Root<InventoryEntity> root = cq.from(InventoryEntity.class);
        CriteriaQuery<InventoryEntity> all = cq.select(root);
        all.where(cb.equal(root.get("hero"), heroId));
        TypedQuery<InventoryEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public InventoryEntity getHeroItemFromInventory(Integer heroId, Integer itemId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InventoryEntity> cq = cb.createQuery(InventoryEntity.class);
        Root<InventoryEntity> root = cq.from(InventoryEntity.class);
        CriteriaQuery<InventoryEntity> all = cq.select(root);
        all.where(cb.equal(root.get("hero"), heroId));
        all.where(cb.equal(root.get("item"), itemId));
        TypedQuery<InventoryEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList().get(0);
    }
}
