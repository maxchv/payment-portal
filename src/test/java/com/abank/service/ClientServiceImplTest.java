package com.abank.service;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.AccountRepository;
import com.abank.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Test
    public void testFindAccountByClientId() {
        // Arrange
        Client client = new Client();
        client.setId(123L);
        client.setFirstName("Jane");
        client.setLastName("Doe");
        Account account1 = new Account();
        account1.setId(1L);
        account1.setClient(client);
        account1.setAccountNum("123");
        account1.setAccountType("simple/card");
        account1.setBalance(BigDecimal.valueOf(1000));
        Account account2 = new Account();
        account2.setId(2L);
        account2.setClient(client);
        account2.setAccountNum("321");
        account2.setAccountType("simple/card");
        account2.setBalance(BigDecimal.valueOf(5000));
        List<Account> accountList = List.of(account1, account2);
        client.setAccounts(accountList);
        Optional<Client> ofResult = Optional.of(client);
        when(this.clientRepository.findById(any())).thenReturn(ofResult);

        // Act
        List<Account> actualFindAccountByClientIdResult = this.clientServiceImpl.findAccountByClientId(123L);

        // Assert
        assertSame(accountList, actualFindAccountByClientIdResult);
        assertFalse(actualFindAccountByClientIdResult.isEmpty());
        assertEquals(2, actualFindAccountByClientIdResult.size());
        assertSame(account1, actualFindAccountByClientIdResult.get(0));
        assertSame(account2, actualFindAccountByClientIdResult.get(1));
        verify(this.clientRepository).findById(any());
    }

    @Test
    public void testFindAccountByNotFoundClient() {
        // Arrange
        when(this.clientRepository.findById(any())).thenReturn(Optional.<Client>empty());

        // Act and Assert
        assertThrows(ClientNotFoundException.class, () -> this.clientServiceImpl.findAccountByClientId(123L));
        verify(this.clientRepository).findById((Long) any());
    }

    @Test
    public void testCreateClient() {
        // Arrange
        Client client = new Client();
        client.setId(123L);
        client.setFirstName("Jane");
        client.setLastName("Doe");
        client.setAccounts(new ArrayList<>());
        when(this.clientRepository.save(any())).thenReturn(client);

        Client client1 = new Client();
        client1.addAccounts(new ArrayList<>());

        // Act and Assert
        assertNull(this.clientServiceImpl.createClient(client1).getClientId());
        verify(this.clientRepository).save(any());
    }
}

