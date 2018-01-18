package com.client.service;

import com.client.domain.db.CryptoKey;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CryptoKeyService extends AbstractService<CryptoKey> {

    public CryptoKey getCryptoKey() {
        List<CryptoKey> list = repository.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            Collections.sort(list);
            return list.get(0);
        }
        return new CryptoKey();
    }
}
