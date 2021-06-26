package com.abank.repository.jpa;

import com.abank.model.Payment;
import com.abank.repository.PaymentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Repository
@Profile("jpa")
public class JpaPaymentRepositoryImpl implements PaymentRepository {

    private final EntityManager entityManager;

    public JpaPaymentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Payment> findAllByPayment(Payment example) {
        /*
        select
            p.payment_id,
            p.amount,
            p.dest_acc_id,
            p.reason,
            p.source_acc_id,
            p.status,
            p.timestamp
        from
            payments p
        inner join
            accounts a1
                on p.dest_acc_id=a1.account_id
        inner join
            clients c1
                on a1.client_fk=c1.client_id
        inner join
            accounts a2
                on p.source_acc_id=a2.account_id
        inner join
            clients c2
                on a2.client_fk=c2.client_id
        where
            c1.client_id=1
            and a1.account_id=2
            and c2.client_id=1
            and a2.account_id=1
         */
        return entityManager
                .createQuery("select p from Payment p " +
                        " join p.destinationAccount a1" +
                        " join a1.client c1" +
                        " join p.sourceAccount a2" +
                        " join a2.client c2" +
                        " where c1.id = ?1 and a1.id = ?2 and c2.id = ?3 and a2.id = ?4", Payment.class)
                .setParameter(1, example.getDestinationAccount().getClient().getId())
                .setParameter(2, example.getDestinationAccount().getId())
                .setParameter(3, example.getSourceAccount().getClient().getId())
                .setParameter(4, example.getSourceAccount().getId())
                .getResultList();
    }

    @Transactional
    @Override
    public Payment save(Payment entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.of(entityManager.find(Payment.class, id));
    }

    @Override
    public List<Payment> findAll() {
        return entityManager.createQuery("select p from Payment p", Payment.class).getResultList();
    }
}
