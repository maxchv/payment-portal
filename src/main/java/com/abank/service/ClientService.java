package com.abank.service;

import com.abank.dto.response.ClientResponse;
import com.abank.model.Account;
import com.abank.model.Client;

import java.util.List;

public interface ClientService {
    List<Account> findAccountByClientId(Long id) throws ClientNotFoundException;

    ClientResponse createClient(Client client);

    List<Client> getAllClients();
}
