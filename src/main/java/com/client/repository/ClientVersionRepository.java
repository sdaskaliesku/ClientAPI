package com.client.repository;

import com.client.domain.db.ClientVersion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * @author sdaskaliesku
 */

@Repository
@Transactional
public class ClientVersionRepository extends AbstractRepository<ClientVersion> {
    private static final Boolean USE_BANNED_VERSIONS = false;

    @Override
    public List<ClientVersion> getAll() {
        try {
            return entityManager.createNamedQuery(ClientVersion.GET_ALL_VERSIONS, ClientVersion.class).getResultList();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public List<ClientVersion> getAllAllowedVersions() {
        try {
            return entityManager.createNamedQuery(ClientVersion.GET_ALL_ALLOWED_VERSIONS, ClientVersion.class)
                    .setParameter("isBanned", USE_BANNED_VERSIONS).getResultList();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public void setAllVersionsBanned(double lastWorkingVersion) {
        List<ClientVersion> clientVersions = getAll();
        for (ClientVersion clientVersion : clientVersions) {
            if (clientVersion.getVersion() < lastWorkingVersion) {
                clientVersion.setBanned(true);
                update(clientVersion);
            }
        }
    }

    public ClientVersion getClientVersion(double version, boolean isBetta) {
        return entityManager.createNamedQuery(ClientVersion.GET_VERSION_BY_VERSION_AND_BETTA, ClientVersion.class)
                .setParameter("isBetta", isBetta)
                .setParameter("version", version)
                .setParameter("isBanned", USE_BANNED_VERSIONS)
                .getSingleResult();
    }

    public ClientVersion getLastVersion(boolean isBetta) {
        try {
            List<ClientVersion> clientVersions = entityManager.createNamedQuery(ClientVersion.GET_ALL_VERSIONS_DESC, ClientVersion.class)
                    .setParameter("isBetta", isBetta)
                    .setParameter("isBanned", USE_BANNED_VERSIONS)
                    .getResultList();
            if (!CollectionUtils.isEmpty(clientVersions)) {
                return clientVersions.get(0);
            }
        } catch (PersistenceException e) {
            // do nothing
        }
        return null;
    }

    public List<ClientVersion> getCurrentVersion(double version) {
        return entityManager.createNamedQuery(ClientVersion.GET_CURRENT_VERSION, ClientVersion.class)
                .setParameter("version", version)
                .setParameter("isBanned", USE_BANNED_VERSIONS)
                .getResultList();
    }

}
