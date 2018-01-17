package com.client.repository;

import com.client.domain.db.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@Transactional
public class AccountRepository extends AbstractRepository<Account> {

    @Inject
    private PasswordEncoder passwordEncoder;

    public Account findByName(String name) {
        try {
            return entityManager.createNamedQuery(Account.FIND_BY_NAME, Account.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    public List<Account> getAll() {
        return entityManager.createNamedQuery(Account.FIND_ALL, Account.class).getResultList();
    }

    @Override
    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.persist(account);
        return account;
    }
}
