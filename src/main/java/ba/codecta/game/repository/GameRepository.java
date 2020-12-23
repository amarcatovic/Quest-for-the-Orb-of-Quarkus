package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.GameEntity;
import ba.codecta.game.repository.entity.InventoryEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class GameRepository extends Repository<GameEntity, Integer> {

    public GameRepository() {
        super(GameEntity.class);
    }

    public List<GameEntity> getAllHeroGames(Integer heroId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GameEntity> cq = cb.createQuery(GameEntity.class);
        Root<GameEntity> root = cq.from(GameEntity.class);
        CriteriaQuery<GameEntity> all = cq.select(root);
        all.where(cb.equal(root.get("hero"), heroId));
        TypedQuery<GameEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
}
