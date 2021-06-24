package com.abank.repository.jdbc;

import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcClientRepositoryImpl implements ClientRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcClientRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingGeneratedKeyColumns("client_id");
    }

    @Transactional
    @Override
    public Client save(Client entity) {
        entity.setId((Long) simpleJdbcInsert
                .executeAndReturnKey(Map.of(
                        "first_name", entity.getFirstName(),
                        "last_name", entity.getLastName()
                )));
        return entity;
    }

    private final static RowMapper<Client> rowMapper = (resultSet, i) -> {
        Client client = new Client();
        client.setId(resultSet.getLong("client_id"));
        client.setFirstName(resultSet.getString("first_name"));
        client.setLastName(resultSet.getString("last_name"));
        return client;
    };


    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(
                jdbcTemplate
                        .queryForObject("SELECT client_id, first_name, last_name from clients where client_id=?",
                                rowMapper, id));
    }

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query("SELECT client_id, first_name, last_name from clients", rowMapper);
    }
}
