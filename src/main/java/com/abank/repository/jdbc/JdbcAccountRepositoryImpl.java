package com.abank.repository.jdbc;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcAccountRepositoryImpl implements AccountRepository {
    public static final String SELECT_ALL = "SELECT account_id, account_num, account_type, balance, client_fk FROM accounts";
    public static final String SELECT_BY_ID = "SELECT account_id, account_num, account_type, balance, client_fk FROM accounts WHERE account_id=?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public static final RowMapper<Account> rowMapper = (resultSet, i) -> {
        Account account = new Account();
        account.setId(resultSet.getLong("account_id"));
        account.setAccountNum(resultSet.getString("account_num"));
        account.setAccountType(resultSet.getString("account_type"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        Client client = new Client();
        client.setId(resultSet.getLong("client_fk"));
        account.setClient(client);
        return account;
    };

    public JdbcAccountRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("accounts")
                .usingGeneratedKeyColumns("account_id");
    }

    @Override
    public Account save(Account entity) {
        Number id = jdbcInsert.executeAndReturnKey(Map.of(
                "account_num", entity.getAccountNum(),
                "account_type", entity.getAccountType(),
                "balance", entity.getBalance(),
                "client_fk", entity.getClient().getId()
        ));
        entity.setId(id.longValue());
        return entity;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        SELECT_BY_ID,
                        rowMapper, id
                )
        );
    }

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }


    @Override
    public void updateBalance(Long id, BigDecimal newBalance) {
        jdbcTemplate.update("UPDATE accounts set balance=? WHERE account_id=?",
                id, newBalance);
    }
}
