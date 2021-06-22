package com.abank.service;

import com.abank.dto.response.ClientResponseDto;
import com.abank.model.Account;
import com.abank.model.Client;

import java.util.List;

public interface ClientService {
    List<Account> findAccountByClientId(Long id) throws ClientNotFoundException;

    ClientResponseDto createClient(Client client);
}
