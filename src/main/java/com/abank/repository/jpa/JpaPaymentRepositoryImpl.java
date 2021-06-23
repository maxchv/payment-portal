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
        throw new RuntimeException("Not implemented yet"); // FIXME
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
        return entityManager.createQuery("from Payment", Payment.class).getResultList();
    }
}
