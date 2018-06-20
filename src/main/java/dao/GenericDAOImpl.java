package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;

public class GenericDAOImpl<T> implements GenericDao<T> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;


    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    protected CriteriaBuilder builder() {
        return session().getCriteriaBuilder();
    }

    protected CriteriaQuery<T> criteriaQuery(CriteriaBuilder builder) {
        return builder.createQuery(entityClass);
    }

    protected Root<T> rootEntity(CriteriaQuery<T> cq) {
        return cq.from(entityClass);
    }

    protected Session session() {
        return sessionFactory.getCurrentSession();
    }

    protected Query createQuery(String query) {
        return session().createQuery(query);
    }

    protected Query createQuery(CriteriaQuery query) {
        return session().createQuery(query);
    }

    @Override
    public void save(T t) {
        session().saveOrUpdate(t);
    }

    @Override
    public void delete(Object obj) {
        session().delete(obj);
    }

    @Override
    public void delete(Long id) {
        delete(session().load(entityClass, id));
    }

    @Override
    public List<T> find() {
        return createQuery("from " + entityClass.getName() + " order by id asc").list();
    }

    @Override
    public T find(Long id) {
        return (T) session().get(entityClass, id);
    }

    @Override
    public void update(T t) {
        session().merge(t);
    }

    @Override
    public long countAll() {
        Object count = createQuery("select count(*) from " + entityClass.getName()).getSingleResult();
        return count != null ? (long) count : 0;
    }

    protected BigDecimal getNumber(Object sum) {
        return sum != null ? (BigDecimal) sum : BigDecimal.ZERO;
    }


}
