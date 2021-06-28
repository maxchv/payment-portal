package com.abank.rest;

import com.abank.dto.response.ClientResponse;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.service.ClientNotFoundException;
import com.abank.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping(path = "/api/v1")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/clients/accounts",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Account>> getAccountsByClientId(@RequestParam(name = "client_id") Long clientId) {
        List<Account> accounts;
        try {
            accounts = clientService.findAccountByClientId(clientId);
        } catch (ClientNotFoundException ex) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping(value = "/clients/{id:[0-9]+}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Client> getClientById(@PathVariable(name = "id") Long id) {

        Optional<Client> optionalClient = clientService.getClientById(id);
        if (optionalClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalClient.get());
    }

    @GetMapping(value = "/clients",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }


    @PostMapping(value = "/clients",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ClientResponse> createClient(@Validated @RequestBody Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build(); // FIXME: correct response
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }
}