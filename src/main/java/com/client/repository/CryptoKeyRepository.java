package com.client.repository;

import com.client.domain.db.CryptoKey;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class CryptoKeyRepository extends AbstractRepository<CryptoKey> {
    @Override
    public List<CryptoKey> getAll() {
        try {
            return entityManager.createNamedQuery(CryptoKey.FIND_ALL, CryptoKey.class).getResultList();
        } catch (PersistenceException e) {
            return new ArrayList<>();
        }
    }
}
