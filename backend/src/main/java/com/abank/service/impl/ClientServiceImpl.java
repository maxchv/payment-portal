package com.abank.service.impl;

import com.abank.dto.response.ClientResponse;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.AccountRepository;
import com.abank.repository.ClientRepository;
import com.abank.service.ClientService;
import com.abank.service.error.ClientNotFoundException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    public ClientServiceImpl(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @SneakyThrows
    @Override
    public List<Account> findAccountByClientId(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            throw new ClientNotFoundException("Client not found by id " + id);
        }
        return optionalClient.get().getAccounts();
    }

    @Transactional
    @Override
    public ClientResponse createClient(Client client) {
        clientRepository.save(client);
        for (Account account : client.getAccounts()) {
            account.setClient(client);
            accountRepository.save(account);
        }

        return new ClientResponse(client.getId());
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
}
