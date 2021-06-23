package com.abank.repository.jpa;

import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaClientRepositoryImpl implements ClientRepository {

    private final EntityManager entityManager;

    public JpaClientRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Client save(Client entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Client.class, id));
    }

    @Override
    public List<Client> findAll() {
        return entityManager.createQuery("from Client", Client.class).getResultList();
    }
}
