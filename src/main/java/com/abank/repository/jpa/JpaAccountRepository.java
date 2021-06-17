package com.abank.repository.jpa;

import com.abank.model.Account;
import com.abank.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaAccountRepository extends JpaRepository<Account, Long>, AccountRepository {
}
