package com.abank.repository.springdata;

import com.abank.model.Account;
import com.abank.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("springdata")
public interface SpringDataAccountRepository extends JpaRepository<Account, Long>, AccountRepository {
}
