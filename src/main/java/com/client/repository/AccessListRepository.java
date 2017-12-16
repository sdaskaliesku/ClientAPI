package com.client.repository;

import com.client.domain.db.AccessList;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Repository
@Transactional
public class AccessListRepository extends AbstractRepository<AccessList> {

    public List<AccessList> getAllClansOrUsers(boolean isClan) {
        return entityManager.createNamedQuery(AccessList.GET_ALL_CLANS_OR_USERS, AccessList.class).setParameter("isClan", isClan).getResultList();
    }

    @Override
    public List<AccessList> getAll() {
        return entityManager.createNamedQuery(AccessList.GET_ALL, AccessList.class).getResultList();
    }

    public List<AccessList> getAccessByClanOrUserName(String userName, String clanName) {
        return entityManager.createNamedQuery(AccessList.GET_BY_CLAN_OR_USERNAME, AccessList.class)
                .setParameter("nickName", userName)
                .setParameter("clanName", clanName)
                .getResultList();
    }
}
