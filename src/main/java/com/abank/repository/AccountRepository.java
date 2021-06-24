package com.abank.repository;

import com.abank.model.Account;

import java.math.BigDecimal;

public interface AccountRepository extends Repository<Account, Long> {
    void updateBalance(Long id, BigDecimal newBalance);
}
