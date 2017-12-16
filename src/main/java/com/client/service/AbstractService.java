package com.client.service;

import com.client.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Service
public abstract class AbstractService<T>{

    @Autowired
    protected AbstractRepository<T> repository;

    public T create(T t) {
        return repository.create(t);
    }

    public List<T> read() {
        return repository.getAll();
    }

    public T update(T t) {
        return repository.update(t);
    }

    public boolean delete(Long id) {
        try {
            repository.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setRepository(AbstractRepository<T> repository) {
        this.repository = repository;
    }
}
