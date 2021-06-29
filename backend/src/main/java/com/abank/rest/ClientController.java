package com.abank.rest;

import com.abank.dto.response.ClientResponse;
import com.abank.dto.response.ErrorResponse;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.service.ClientService;
import com.abank.service.error.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<Account>> getAccountsByClientId(@RequestParam(name = "client_id") Long clientId)
            throws ClientNotFoundException {
        return ResponseEntity.ok(clientService.findAccountByClientId(clientId));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ClientNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleClientNotFoundExceptions(ClientNotFoundException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("client_id", "Not found"));
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
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }
}