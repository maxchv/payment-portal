package com.abank.repository.jpa;

import com.abank.model.Account;
import com.abank.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaAccountRepositoryImpl implements AccountRepository {
    private final EntityManager entityManager;

    public JpaAccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Account save(Account entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.of(entityManager.find(Account.class, id));
    }

    @Override
    public List<Account> findAll() {
        return entityManager.createQuery("select a from Account a", Account.class).getResultList();
    }


    @Transactional
    @Override
    public void updateBalance(Long id, BigDecimal newBalance) {
        Account account = entityManager.find(Account.class, id);
        account.setBalance(newBalance);
        entityManager.merge(account);
    }
}
