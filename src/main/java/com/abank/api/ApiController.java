package com.abank.api;

import java.util.List;

import com.abank.data.entity.Client;
import com.abank.data.repository.ClientRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1")
@RestController
public class ApiController {
    private final ClientRepository clientRepository;

    public ApiController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public List<Client> allClients() {
        return clientRepository.findAll();
    }
}