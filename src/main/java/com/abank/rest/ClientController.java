package com.abank.rest;

import com.abank.dto.response.ClientResponseDto;
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

@RestController
@RequestMapping(path = "/api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Account>> getClientById(@RequestParam(name = "client_id") Long clientId) {
        List<Account> accounts;
        try {
            accounts = clientService.findAccountByClientId(clientId);
        } catch (ClientNotFoundException ex) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ClientResponseDto> createClient(@Validated @RequestBody Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build(); // FIXME: correct response
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }
}