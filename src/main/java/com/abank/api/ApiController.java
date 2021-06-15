package com.abank.api;

import com.abank.data.entity.Account;
import com.abank.data.entity.Client;
import com.abank.data.repository.ClientRepository;
import com.abank.viewmodel.CreateClientView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path = "/api/v1")
@RestController
public class ApiController {
    private final ClientRepository clientRepository;

    public ApiController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

//    @GetMapping("/clients")
//    public List<Client> allClients() {
//        return clientRepository.findAll();
//    }

    @GetMapping(value = "/clients", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Account>> getClientById(@RequestParam(name = "client_id") Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(optionalClient.orElseThrow().getAccounts());
    }

    @PostMapping(value = "/clients", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateClientView> createClient(@Validated @RequestBody Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build(); // FIXME: correct response
        }
        for (Account account : client.getAccounts()) {
            account.setClient(client);
        }
        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateClientView(client.getId()));
    }
}