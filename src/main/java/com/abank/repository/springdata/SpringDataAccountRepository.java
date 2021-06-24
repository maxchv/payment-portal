package com.abank.repository.springdata;

import com.abank.model.Account;
import com.abank.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

@Profile("springdata")
public interface SpringDataAccountRepository extends JpaRepository<Account, Long>, AccountRepository {
    @Modifying
    @Query("update Account a set a.balance=:balance where a.id=:id")
    void updateBalance(@Param("id") Long id, @Param("balance") BigDecimal newBalance);
}
