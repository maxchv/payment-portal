package com.abank.repository.jdbc;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcClientRepositoryImpl implements ClientRepository {

    public static final String SELECT_ALL = "SELECT client_id, first_name, last_name from clients";
    public static final String SELECT_BY_ID = "SELECT client_id, first_name, last_name from clients where client_id=?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    public final static RowMapper<Client> rowMapper = (resultSet, i) -> {
        Client client = new Client();
        client.setId(resultSet.getLong("client_id"));
        client.setFirstName(resultSet.getString("first_name"));
        client.setLastName(resultSet.getString("last_name"));
        return client;
    };

    public JdbcClientRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingGeneratedKeyColumns("client_id");
    }

    @Override
    public Client save(Client entity) {
        Number id = jdbcInsert
                .executeAndReturnKey(Map.of(
                        "first_name", entity.getFirstName(),
                        "last_name", entity.getLastName()
                ));
        entity.setId(id.longValue());
        return entity;
    }

    @Override
    public Optional<Client> findById(Long id) {
        Client client = jdbcTemplate
                .queryForObject(SELECT_BY_ID, rowMapper, id);
        List<Account> accounts = jdbcTemplate.query("SELECT account_id, account_num, account_type, balance, client_fk FROM accounts WHERE client_fk=?",
                JdbcAccountRepositoryImpl.rowMapper, id);
        accounts.forEach(a -> a.setClient(client));
        client.setAccounts(accounts);
        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }
}
