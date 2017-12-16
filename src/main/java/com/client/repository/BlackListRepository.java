package com.client.repository;

import com.client.domain.db.BlackList;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Repository
@Transactional
public class BlackListRepository extends AbstractRepository<BlackList> {
    @Override
    public List<BlackList> getAll() {
        return entityManager.createNamedQuery(BlackList.GET_ALL, BlackList.class).getResultList();
    }

    public List<BlackList> getAllClans() {
        return getAllClansOrUsers(true);
    }

    public List<BlackList> getAllUsers() {
        return getAllClansOrUsers(false);
    }

    public BlackList isUserOrClanInBlackList(String usernameOrClanname) {
        return entityManager.createNamedQuery(BlackList.IS_USER_IN_BLACK_LIST, BlackList.class)
                .setParameter("nickOrClanName", usernameOrClanname).getSingleResult();
    }

    private List<BlackList> getAllClansOrUsers(boolean isClan) {
        return entityManager.createNamedQuery(BlackList.GET_ALL_CLANS_OR_USERS, BlackList.class)
                .setParameter("isClan", isClan).getResultList();
    }
}
