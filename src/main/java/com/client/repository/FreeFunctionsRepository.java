package com.client.repository;

import com.client.domain.db.FreeFunctions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class FreeFunctionsRepository extends AbstractRepository<FreeFunctions> {
    @Override
    public List<FreeFunctions> getAll() {
        return entityManager.createNamedQuery(FreeFunctions.FIND_ALL, FreeFunctions.class).getResultList();
    }
}
