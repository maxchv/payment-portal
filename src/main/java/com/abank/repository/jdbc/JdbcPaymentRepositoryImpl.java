package com.abank.repository.jdbc;

import com.abank.model.Account;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.PaymentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Repository
@Profile("jdbc")
public class JdbcPaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Payment> rowMapper = (resultSet, i) -> {
        Payment payment = new Payment();
        payment.setId(resultSet.getLong("payment_id"));

        Account source = new Account();
        source.setId(resultSet.getLong("source_acc_id"));
        payment.setSourceAccount(source);

        Account destination = new Account();
        destination.setId(resultSet.getLong("dest_acc_id"));
        payment.setDestinationAccount(destination);

        payment.setAmount(resultSet.getBigDecimal("amount"));
        payment.setTimestamp(LocalDateTime.parse(resultSet.getString("timestamp"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
        payment.setReason(resultSet.getString("reason"));
        payment.setStatus(PaymentStatus.valueOf(resultSet.getString("status")));

        return payment;
    };

    public JdbcPaymentRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("payments")
                .usingGeneratedKeyColumns("payment_id");
    }


    @Override
    public List<Payment> findAllByPayment(Payment example) {

        var sql = "select p.payment_id, p.amount, p.dest_acc_id, p.reason, p.source_acc_id, p.status, p.timestamp"
                + " from payments p inner"
                + " join accounts a1 on p.dest_acc_id=a1.account_id"
                + " inner join clients c1 on a1.client_fk=c1.client_id"
                + " inner join accounts a2 on p.source_acc_id=a2.account_id"
                + " inner join clients c2 on a2.client_fk=c2.client_id"
                + " where c1.client_id=? and a1.account_id=? and c2.client_id=? and a2.account_id=?";

        var payments =
                jdbcTemplate.query(sql, rowMapper,
                        example.getDestinationAccount().getClient().getId(),
                        example.getDestinationAccount().getId(),
                        example.getSourceAccount().getClient().getId(),
                        example.getSourceAccount().getId());
        for (Payment p : payments) {
            var source = jdbcTemplate.queryForObject(JdbcAccountRepositoryImpl.SELECT_BY_ID,
                    JdbcAccountRepositoryImpl.rowMapper, p.getSourceAccount().getId());
            p.setSourceAccount(source);
            var sourceClient = jdbcTemplate.queryForObject(JdbcClientRepositoryImpl.SELECT_BY_ID,
                    JdbcClientRepositoryImpl.rowMapper, source.getClient().getId());
            source.setClient(sourceClient);
            var destination = jdbcTemplate.queryForObject(JdbcAccountRepositoryImpl.SELECT_BY_ID,
                    JdbcAccountRepositoryImpl.rowMapper, p.getDestinationAccount().getId());
            p.setDestinationAccount(destination);
            var destinationClient = jdbcTemplate.queryForObject(JdbcClientRepositoryImpl.SELECT_BY_ID,
                    JdbcClientRepositoryImpl.rowMapper, destination.getClient().getId());
            destination.setClient(destinationClient);
        }
        return payments;
    }

    @Override
    public Payment save(Payment entity) {
        var params = new MapSqlParameterSource();
        params.addValue("source_acc_id", entity.getSourceAccount().getId());
        params.addValue("dest_acc_id", entity.getDestinationAccount().getId());
        params.addValue("amount", entity.getAmount());
        params.addValue("timestamp", LocalDateTime.now());
        params.addValue("reason", entity.getReason());
        params.addValue("status", entity.getStatus());
        Number id = jdbcInsert.executeAndReturnKey(params);
        entity.setId(id.longValue());

        return entity;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT payment_id, source_acc_id, dest_acc_id, amount, timestamp, reason, status WHERE payment_id=?",
                        rowMapper, id)
        );
    }

    @Override
    public List<Payment> findAll() {
        return jdbcTemplate.query("SELECT payment_id, source_acc_id, dest_acc_id, amount, timestamp, reason, status", rowMapper);
    }
}
