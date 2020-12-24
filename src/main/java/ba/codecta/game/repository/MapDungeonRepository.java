package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.MapDungeonEntity;
import ba.codecta.game.services.model.MapDungeonDto;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class MapDungeonRepository extends Repository<MapDungeonEntity, Integer>{
    public MapDungeonRepository() {
        super(MapDungeonEntity.class);
    }

    public List<MapDungeonEntity> getMapDungeons(Integer mapId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MapDungeonEntity> cq = cb.createQuery(MapDungeonEntity.class);
        Root<MapDungeonEntity> root = cq.from(MapDungeonEntity.class);
        CriteriaQuery<MapDungeonEntity> all = cq.select(root);
        all.where(cb.equal(root.get("map"), mapId));
        TypedQuery<MapDungeonEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public MapDungeonEntity getCurrentPlayerDungeonLocation(Integer mapId, Integer x, Integer y){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MapDungeonEntity> cq = cb.createQuery(MapDungeonEntity.class);
        Root<MapDungeonEntity> root = cq.from(MapDungeonEntity.class);
        CriteriaQuery<MapDungeonEntity> all = cq.select(root);
        all.where(cb.equal(root.get("map"), mapId));
        all.where(cb.equal(root.get("locationX"), x), cb.equal(root.get("locationY"), y));
        TypedQuery<MapDungeonEntity> allQuery = entityManager.createQuery(all);
        return allQuery.getSingleResult();
    }
}
