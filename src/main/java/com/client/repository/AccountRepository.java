package com.client.repository;

import javax.persistence.*;
import javax.inject.Inject;

import com.client.domain.db.Account;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Deprecated
    public List<Account> getAll() {
        throw new NotYetImplementedException();
    }

    @Override
    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.persist(account);
        return account;
    }
}
