package com.abank.service;

import com.abank.dto.request.PaymentRequest;
import com.abank.dto.request.PaymentRequestInfo;
import com.abank.dto.response.PaymentResponseInfo;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.AccountRepository;
import com.abank.repository.PaymentRepository;
import com.abank.service.mapping.MappingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PaymentServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MappingUtils mappingUtils;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Test
    public void testCreatePayment() throws AccountNotFoundException, NotEnoughMoney {
        // Arrange
        when(this.accountRepository.findById((Long) any())).thenReturn(Optional.<Account>empty());

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> this.paymentServiceImpl.createPayment(new PaymentRequest()));
        verify(this.accountRepository, times(2)).findById((Long) any());
    }

    @Test
    public void testCreatePayment3() throws AccountNotFoundException, NotEnoughMoney {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        client.setAccounts(new ArrayList<>());
        client.setFirstName("Jane");

        Account account = new Account();
        account.setClient(client);
        account.setId(123L);
        account.setAccountNum("Account Num");
        account.setBalance(BigDecimal.valueOf(42L));
        account.setAccountType("Account Type");

        Client client1 = new Client();
        client1.setLastName("Doe");
        client1.setId(123L);
        client1.setAccounts(new ArrayList<>());
        client1.setFirstName("Jane");

        Account account1 = new Account();
        account1.setClient(client1);
        account1.setId(123L);
        account1.setAccountNum("Account Num");
        account1.setBalance(BigDecimal.valueOf(42L));
        account1.setAccountType("Account Type");

        Payment payment = new Payment();
        payment.setDestinationAccount(account);
        payment.setReason("Just cause");
        payment.setAmount(BigDecimal.valueOf(42L));
        payment.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        payment.setStatus(PaymentStatus.ok);
        payment.setId(123L);
        payment.setSourceAccount(account1);
        when(this.paymentRepository.save(any())).thenReturn(payment);

        Client client2 = new Client();
        client2.setLastName("Doe");
        client2.setId(123L);
        client2.setAccounts(new ArrayList<>());
        client2.setFirstName("Jane");

        Account account2 = new Account();
        account2.setClient(client2);
        account2.setId(123L);
        account2.setAccountNum("Account Num");
        account2.setBalance(BigDecimal.valueOf(0L));
        account2.setAccountType("Account Type");
        Optional<Account> ofResult = Optional.of(account2);

        Client client3 = new Client();
        client3.setLastName("Doe");
        client3.setId(123L);
        client3.setAccounts(new ArrayList<>());
        client3.setFirstName("Jane");

        Account account3 = new Account();
        account3.setClient(client3);
        account3.setId(123L);
        account3.setAccountNum("Account Num");
        account3.setBalance(BigDecimal.valueOf(42L));
        account3.setAccountType("Account Type");
        when(this.accountRepository.save(any())).thenReturn(account3);
        when(this.accountRepository.findById(any())).thenReturn(ofResult);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(42L));

        // Act and Assert
        assertThrows(NotEnoughMoney.class, () -> this.paymentServiceImpl.createPayment(paymentRequest));
        verify(this.paymentRepository).save((Payment) any());
        verify(this.accountRepository, times(2)).findById((Long) any());
    }

    @Test
    public void testCreatePayment4() throws AccountNotFoundException, NotEnoughMoney {
        // Arrange
        when(this.accountRepository.findById((Long) any())).thenReturn(Optional.<Account>empty());

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> this.paymentServiceImpl.createPayment(new PaymentRequest()));
        verify(this.accountRepository, times(2)).findById((Long) any());
    }

    @Test
    public void testCreatePayment5() throws AccountNotFoundException, NotEnoughMoney {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        client.setAccounts(new ArrayList<Account>());
        client.setFirstName("Jane");

        Account account = new Account();
        account.setClient(client);
        account.setId(123L);
        account.setAccountNum("Account Num");
        account.setBalance(BigDecimal.valueOf(42L));
        account.setAccountType("Account Type");

        Client client1 = new Client();
        client1.setLastName("Doe");
        client1.setId(123L);
        client1.setAccounts(new ArrayList<Account>());
        client1.setFirstName("Jane");

        Account account1 = new Account();
        account1.setClient(client1);
        account1.setId(123L);
        account1.setAccountNum("Account Num");
        account1.setBalance(BigDecimal.valueOf(42L));
        account1.setAccountType("Account Type");

        Payment payment = new Payment();
        payment.setDestinationAccount(account);
        payment.setReason("Just cause");
        payment.setAmount(BigDecimal.valueOf(42L));
        payment.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        payment.setStatus(PaymentStatus.ok);
        payment.setId(123L);
        payment.setSourceAccount(account1);
        when(this.paymentRepository.save((Payment) any())).thenReturn(payment);

        Client client2 = new Client();
        client2.setLastName("Doe");
        client2.setId(123L);
        client2.setAccounts(new ArrayList<Account>());
        client2.setFirstName("Jane");

        Account account2 = new Account();
        account2.setClient(client2);
        account2.setId(123L);
        account2.setAccountNum("Account Num");
        account2.setBalance(BigDecimal.valueOf(42L));
        account2.setAccountType("Account Type");
        Optional<Account> ofResult = Optional.<Account>of(account2);
        doNothing().when(this.accountRepository).updateBalance((Long) any(), (BigDecimal) any());
        when(this.accountRepository.findById((Long) any())).thenReturn(ofResult);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(42L));

        // Act and Assert
        assertNull(this.paymentServiceImpl.createPayment(paymentRequest).getPaymentId());
        verify(this.paymentRepository).save((Payment) any());
        verify(this.accountRepository, times(2)).findById((Long) any());
        verify(this.accountRepository, times(2)).updateBalance((Long) any(), (BigDecimal) any());
    }

    @Test
    public void testCreatePayment6() throws AccountNotFoundException, NotEnoughMoney {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        client.setAccounts(new ArrayList<Account>());
        client.setFirstName("Jane");

        Account account = new Account();
        account.setClient(client);
        account.setId(123L);
        account.setAccountNum("Account Num");
        account.setBalance(BigDecimal.valueOf(42L));
        account.setAccountType("Account Type");

        Client client1 = new Client();
        client1.setLastName("Doe");
        client1.setId(123L);
        client1.setAccounts(new ArrayList<Account>());
        client1.setFirstName("Jane");

        Account account1 = new Account();
        account1.setClient(client1);
        account1.setId(123L);
        account1.setAccountNum("Account Num");
        account1.setBalance(BigDecimal.valueOf(42L));
        account1.setAccountType("Account Type");

        Payment payment = new Payment();
        payment.setDestinationAccount(account);
        payment.setReason("Just cause");
        payment.setAmount(BigDecimal.valueOf(42L));
        payment.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        payment.setStatus(PaymentStatus.ok);
        payment.setId(123L);
        payment.setSourceAccount(account1);
        when(this.paymentRepository.save((Payment) any())).thenReturn(payment);

        Client client2 = new Client();
        client2.setLastName("Doe");
        client2.setId(123L);
        client2.setAccounts(new ArrayList<Account>());
        client2.setFirstName("Jane");

        Account account2 = new Account();
        account2.setClient(client2);
        account2.setId(123L);
        account2.setAccountNum("Account Num");
        account2.setBalance(BigDecimal.valueOf(0L));
        account2.setAccountType("Account Type");
        Optional<Account> ofResult = Optional.<Account>of(account2);
        doNothing().when(this.accountRepository).updateBalance((Long) any(), (BigDecimal) any());
        when(this.accountRepository.findById((Long) any())).thenReturn(ofResult);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(42L));

        // Act and Assert
        assertThrows(NotEnoughMoney.class, () -> this.paymentServiceImpl.createPayment(paymentRequest));
        verify(this.paymentRepository).save((Payment) any());
        verify(this.accountRepository, times(2)).findById((Long) any());
    }

    @Test
    public void testCreatePayments() {
        // Arrange
        when(this.accountRepository.findById((Long) any())).thenReturn(Optional.<Account>empty());
        PaymentRequest paymentRequest = new PaymentRequest();
        PaymentRequest paymentRequest1 = new PaymentRequest();

        // Act and Assert
        assertTrue(this.paymentServiceImpl
                .createPayments(new PaymentRequest[]{paymentRequest, paymentRequest1, new PaymentRequest()})
                .isEmpty());
        verify(this.accountRepository, times(6)).findById((Long) any());
    }

    @Test
    public void testGetPaymentInfo() {
        // Arrange
        when(this.paymentRepository.findAllByPayment((Payment) any())).thenReturn(new ArrayList<Payment>());

        // Act and Assert
        assertTrue(this.paymentServiceImpl.getPaymentInfo(new PaymentRequestInfo()).isEmpty());
        verify(this.paymentRepository).findAllByPayment((Payment) any());
    }

    @Test
    public void testGetPaymentInfo2() {
        // Arrange
        Client client = new Client();
        client.setLastName("Doe");
        client.setId(123L);
        client.setAccounts(new ArrayList<Account>());
        client.setFirstName("Jane");

        Account account = new Account();
        account.setClient(client);
        account.setId(123L);
        account.setAccountNum("Account Num");
        account.setBalance(BigDecimal.valueOf(42L));
        account.setAccountType("Account Type");

        Client client1 = new Client();
        client1.setLastName("Doe");
        client1.setId(123L);
        client1.setAccounts(new ArrayList<Account>());
        client1.setFirstName("Jane");

        Account account1 = new Account();
        account1.setClient(client1);
        account1.setId(123L);
        account1.setAccountNum("Account Num");
        account1.setBalance(BigDecimal.valueOf(42L));
        account1.setAccountType("Account Type");

        Payment payment = new Payment();
        payment.setDestinationAccount(account);
        payment.setReason("Just cause");
        payment.setAmount(BigDecimal.valueOf(42L));
        payment.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        payment.setStatus(PaymentStatus.ok);
        payment.setId(123L);
        payment.setSourceAccount(account1);

        ArrayList<Payment> paymentList = new ArrayList<Payment>();
        paymentList.add(payment);
        when(this.paymentRepository.findAllByPayment((Payment) any())).thenReturn(paymentList);
        when(this.mappingUtils.paymentEntityToResponseDto((Payment) any())).thenReturn(mock(PaymentResponseInfo.class));

        // Act and Assert
        assertEquals(1, this.paymentServiceImpl.getPaymentInfo(new PaymentRequestInfo()).size());
        verify(this.paymentRepository).findAllByPayment((Payment) any());
        verify(this.mappingUtils).paymentEntityToResponseDto((Payment) any());
    }
}

