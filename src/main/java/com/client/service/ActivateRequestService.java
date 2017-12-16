package com.client.service;

import com.client.domain.db.ActivateRequest;
import com.client.repository.ActivateRequestRepository;
import org.springframework.stereotype.Service;

/**
 * @author sdaskaliesku
 */
@Service
public class ActivateRequestService extends AbstractService<ActivateRequest> {
    private ActivateRequestRepository getRepository() {
        return (ActivateRequestRepository) repository;
    }
}
