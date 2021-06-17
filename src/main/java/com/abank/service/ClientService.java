package com.abank.service;

import com.abank.dto.CreateClientDto;
import com.abank.model.Account;
import com.abank.model.Client;

import java.util.List;

public interface ClientService {
    List<Account> findAccountByClientId(Long id) throws ClientNotFoundException;
    CreateClientDto createClient(Client client);
}
