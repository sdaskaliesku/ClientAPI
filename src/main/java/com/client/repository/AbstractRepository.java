package com.client.repository;

import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author sdaskaliesku
 */
public abstract class AbstractRepository<T> {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public abstract List<T> getAll();

    @Transactional
    public T create(T t) {
        entityManager.persist(t);
        return t;
    }

    @Transactional
    public T update(T t) {
        return entityManager.merge(t);
    }

    @Transactional
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Transactional
    public void delete(Long id) {
        Class<T> tClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractRepository.class);
        T t = entityManager.find(tClass, id);
        delete(t);
    }
}
