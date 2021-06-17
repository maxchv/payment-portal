package com.abank.repository.jdbc;

import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Profile("jdbc")
@Repository
public class JdbcClientRepository implements ClientRepository {

    private final EntityManager entityManager;

    public JdbcClientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Client save(Client entity) {
        return null;
    }

    @Override
    public Optional<Client> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return null;
    }
}
