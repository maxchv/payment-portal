package com.abank.service;

import com.abank.dto.response.ClientResponseDto;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

    @Override
    public ClientResponseDto createClient(Client client) {
        for (Account account : client.getAccounts()) {
            account.setClient(client);
        }
        clientRepository.save(client);
        return new ClientResponseDto(client.getId());
    }
}
