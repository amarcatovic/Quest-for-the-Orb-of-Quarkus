package ba.codecta.game.repository;

import ba.codecta.game.repository.entity.ModelObject;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Repository<T extends ModelObject, PK extends Serializable> {
    private Class<T> entityClass;

    protected Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Inject
    EntityManager entityManager;

    public T add(T modelObject)
    {
        modelObject.setCreatedOn(LocalDateTime.now());
        modelObject.setModifiedOn(LocalDateTime.now());
        entityManager.persist(modelObject);
        return modelObject;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
        Root<T> root = cq.from(this.entityClass);
        CriteriaQuery<T> all = cq.select(root);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public T findById(PK id)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
        Root<T> root = cq.from(this.entityClass);
        CriteriaQuery<T> all = cq.select(root);
        all.where(cb.equal(root.get("id"), id));
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getSingleResult();
    }

    public T save(T modelObject)
    {
        T result = null;
        PK id = (PK)modelObject.getId();
        if(id != null) {
            result = findById(id);
        }
        if(id == null || result!= null) {
            entityManager.persist(modelObject);
            return modelObject;
        }
        return null;
    }

    public void delete(T modelObject){
        entityManager.remove(modelObject);
    }
}
