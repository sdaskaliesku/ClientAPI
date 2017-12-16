package com.client.repository;

import com.client.domain.db.ActivateRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Repository
@Transactional
public class ActivateRequestRepository extends AbstractRepository<ActivateRequest> {

    @Override
    public List<ActivateRequest> getAll() {
        return entityManager.createNamedQuery(ActivateRequest.GET_LOGS, ActivateRequest.class).getResultList();
    }

}
