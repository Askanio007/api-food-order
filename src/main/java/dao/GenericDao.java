package dao;

import java.util.List;

public interface GenericDao<T> {

    void save(T t);

    void delete(Object obj);

    void delete(Long id);

    List<T> find();

    T find(Long id);

    void update(T t);

    long countAll();
}
