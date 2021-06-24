package com.abank.service;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ClientServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class ClientServiceImplTest {
    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientServiceImpl clientServiceImpl;

    @Test
    public void testFindAccountByClientId() {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        ArrayList<Account> accountList = new ArrayList<Account>();
        client.setAccounts(accountList);
        client.setFirstName("Jane");
        Optional<Client> ofResult = Optional.<Client>of(client);
        when(this.clientRepository.findById((Long) any())).thenReturn(ofResult);

        // Act
        List<Account> actualFindAccountByClientIdResult = this.clientServiceImpl.findAccountByClientId(123L);

        // Assert
        assertSame(accountList, actualFindAccountByClientIdResult);
        assertTrue(actualFindAccountByClientIdResult.isEmpty());
        verify(this.clientRepository).findById((Long) any());
    }

    @Test
    public void testFindAccountByClientId2() {
        // Arrange
        when(this.clientRepository.findById((Long) any())).thenReturn(Optional.<Client>empty());

        // Act and Assert
        assertThrows(ClientNotFoundException.class, () -> this.clientServiceImpl.findAccountByClientId(123L));
        verify(this.clientRepository).findById((Long) any());
    }

    @Test
    public void testCreateClient() {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        client.setAccounts(new ArrayList<Account>());
        client.setFirstName("Jane");
        when(this.clientRepository.save((Client) any())).thenReturn(client);

        Client client1 = new Client();
        client1.addAccounts(new ArrayList<>());

        // Act and Assert
        assertNull(this.clientServiceImpl.createClient(client1).getClientId());
        verify(this.clientRepository).save((Client) any());
    }
}

