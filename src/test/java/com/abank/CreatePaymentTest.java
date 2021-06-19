package com.abank;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.AccountRepository;
import com.abank.repository.ClientRepository;
import com.abank.repository.PaymentRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "jpa"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreatePaymentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        Client client = new Client();
        client.setFirstName("Имя");
        client.setLastName("Фамилия");
        Account account1 = new Account();
        account1.setAccountNum("123456789");
        account1.setAccountType("card/simple");
        account1.setBalance(BigDecimal.valueOf(5000));
        Account account2 = new Account();
        account2.setAccountNum("987654321");
        account2.setAccountType("card/simple");
        account2.setBalance(BigDecimal.valueOf(10000));
        client.addAccount(account1);
        client.addAccount(account2);
        clientRepository.save(client);
    }

    @SneakyThrows
    @Test
    @Transactional
    @Order(1)
    void createOneSuccessPaymentResponseTest() {
        var endpoint = "/api/v1/payment";
        var request = "{\n" +
                "  \"source_acc_id\": 1,\n" +
                "  \"dest_acc_id\": 2,\n" +
                "  \"amount\": 100.00,\n" +
                "  \"reason\": \"назначение платежа\"\n" +
                "}";
        mockMvc
                .perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("payment_id").value(1));
    }

    @SneakyThrows
    @Test
    @Transactional
    @Order(2)
    void createOneSuccessPaymentDatabaseTest() {
        var endpoint = "/api/v1/payment";
        var request = "{\n" +
                "  \"source_acc_id\": 1,\n" +
                "  \"dest_acc_id\": 2,\n" +
                "  \"amount\": 100.00,\n" +
                "  \"reason\": \"назначение платежа\"\n" +
                "}";
        mockMvc
                .perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        Optional<Payment> payment = paymentRepository.findById(1L);
        assertTrue(payment.isPresent());
        Payment payment1 = payment.get();
        assertEquals(payment1.getId(), 1L);
        assertEquals(payment1.getSourceAccount().getId(), 1L);
        assertEquals(payment1.getDestinationAccount().getId(), 2L);
        assertEquals(payment1.getAmount(), new BigDecimal("100.00"));
        assertEquals(payment1.getReason(), "назначение платежа");
        assertEquals(payment1.getStatus(), PaymentStatus.ok);

        Optional<Account> source = accountRepository.findById(1L);
        assertTrue(source.isPresent());
        Account sourceAccount = source.get();
        assertEquals(sourceAccount.getBalance(), new BigDecimal("4900.00"));

        Optional<Account> destination = accountRepository.findById(2L);
        assertTrue(destination.isPresent());
        Account destinationAccount = destination.get();
        assertEquals(destinationAccount.getBalance(), new BigDecimal("10100.00"));
    }

    @Test
    @Order(3)
    void createOneFailPaymentResponseTest() {
        fail("Not implemented yet"); // FIXME: реализовать тест
    }

    @Test
    @Order(4)
    void createOneFailPaymentDatabaseTest() {
        fail("Not implemented yet"); // FIXME: реализовать тест
    }

    @SneakyThrows
    @Test
    @Transactional
    @Order(5)
    void createTwoSuccessPaymentsResponseTest() {
        var endpoint = "/api/v1/payments";
        var request = "[\n" +
                "  {\n" +
                "    \"source_acc_id\": 1,\n" +
                "    \"dest_acc_id\": 2,\n" +
                "    \"amount\": 100.00,\n" +
                "    \"reason\": \"назначение платежа\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"source_acc_id\": 2,\n" +
                "    \"dest_acc_id\": 1,\n" +
                "    \"amount\": 1000.00,\n" +
                "    \"reason\": \"назначение платежа\"\n" +
                "  }\n" +
                "]";
        mockMvc
                .perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].payment_id").value(1))
                .andExpect(jsonPath("$.[0].status").value("ok"))
                .andExpect(jsonPath("$.[1].payment_id").value(2))
                .andExpect(jsonPath("$.[1].status").value("ok"));
    }

    @SneakyThrows
    @Test
    @Transactional
    @Order(5)
    void createTwoSuccessPaymentDatabaseTest() {
        var endpoint = "/api/v1/payments";
        var request = "[\n" +
                "  {\n" +
                "    \"source_acc_id\": 1,\n" +
                "    \"dest_acc_id\": 2,\n" +
                "    \"amount\": 100.00,\n" +
                "    \"reason\": \"назначение платежа\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"source_acc_id\": 2,\n" +
                "    \"dest_acc_id\": 1,\n" +
                "    \"amount\": 1000.00,\n" +
                "    \"reason\": \"назначение платежа\"\n" +
                "  }\n" +
                "]";
        mockMvc
                .perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Payment> payment = paymentRepository.findById(1L);
        assertTrue(payment.isPresent());
        Payment payment1 = payment.get();
        assertEquals(payment1.getId(), 1L);
        assertEquals(payment1.getSourceAccount().getId(), 1L);
        assertEquals(payment1.getDestinationAccount().getId(), 2L);
        assertEquals(payment1.getAmount(), new BigDecimal("100.00"));
        assertEquals(payment1.getReason(), "назначение платежа");
        assertEquals(payment1.getStatus(), PaymentStatus.ok);

        payment = paymentRepository.findById(2L);
        assertTrue(payment.isPresent());
        Payment payment2 = payment.get();
        assertEquals(payment2.getId(), 2L);
        assertEquals(payment2.getSourceAccount().getId(), 2L);
        assertEquals(payment2.getDestinationAccount().getId(), 1L);
        assertEquals(payment2.getAmount(), new BigDecimal("1000.00"));
        assertEquals(payment2.getReason(), "назначение платежа");
        assertEquals(payment2.getStatus(), PaymentStatus.ok);

        Optional<Account> source = accountRepository.findById(1L);
        assertTrue(source.isPresent());
        Account sourceAccount = source.get();
        assertEquals(sourceAccount.getBalance(), new BigDecimal("5900.00"));

        Optional<Account> destination = accountRepository.findById(2L);
        assertTrue(destination.isPresent());
        Account destinationAccount = destination.get();
        assertEquals(destinationAccount.getBalance(), new BigDecimal("9100.00"));
    }

    @Test
    @Order(6)
    void createTwoFailPaymentResponseTest() {
        fail("Not implemented yet"); // FIXME: реализовать тест
    }

    @Test
    @Order(7)
    void createTwoFailPaymentDatabaseTest() {
        fail("Not implemented yet"); // FIXME: реализовать тест
    }

}
