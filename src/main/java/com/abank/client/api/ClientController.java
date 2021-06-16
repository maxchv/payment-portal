package com.abank.client.api;

import com.abank.client.account.Account;
import com.abank.client.Client;
import com.abank.client.repository.ClientRepository;
import com.abank.client.dto.CreateClientDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/clients")
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Account>> getClientById(@RequestParam(name = "client_id") Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(optionalClient.orElseThrow().getAccounts());
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateClientDto> createClient(@Validated @RequestBody Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build(); // FIXME: correct response
        }
        for (Account account : client.getAccounts()) {
            account.setClient(client);
        }
        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateClientDto(client.getId()));
    }
}