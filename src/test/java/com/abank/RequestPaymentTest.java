package com.abank;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.ClientRepository;
import com.abank.repository.PaymentRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test", "springdata"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RequestPaymentTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void init() {
        Client client = new Client();
        client.setFirstName("Имя");
        client.setLastName("Фамилия");

        Account account1 = new Account();
        account1.setAccountNum("123456789");
        account1.setAccountType("card/simple");
        account1.setBalance(BigDecimal.valueOf(4900));

        Account account2 = new Account();
        account2.setAccountNum("987654321");
        account2.setAccountType("card/simple");
        account2.setBalance(BigDecimal.valueOf(10100));

        client.addAccount(account1);
        client.addAccount(account2);
        clientRepository.save(client);

        Payment paymentSuccess = new Payment();
        paymentSuccess.setSourceAccount(account1);
        paymentSuccess.setDestinationAccount(account2);
        paymentSuccess.setStatus(PaymentStatus.ok);
        paymentSuccess.setAmount(BigDecimal.valueOf(100));
        paymentSuccess.setReason("Перевод средств");
        paymentRepository.save(paymentSuccess);

        Payment paymentFail = new Payment();
        paymentFail.setSourceAccount(account2);
        paymentFail.setDestinationAccount(account1);
        paymentFail.setStatus(PaymentStatus.error);
        paymentFail.setAmount(BigDecimal.valueOf(100_000));
        paymentFail.setReason("Ошибка перевода");
        paymentRepository.save(paymentFail);
    }

    @SneakyThrows
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void requestInfo() {
        var endpoint = "/api/v1/payments/info";
        var request = "{\n" +
                "  \"payer_id\": 1,\n" +
                "  \"recipient_id\": 1,\n" +
                "  \"source_acc_id\": 1,\n" +
                "  \"dest_acc_id\": 2\n" +
                "}";

        mockMvc
                .perform(
                        post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].payment_id").isNumber())
                .andExpect(jsonPath("$[0].payment_id").value(1))
                .andExpect(jsonPath("$[0].timestamp").isString())
                .andExpect(jsonPath("$[0].src_acc_num").isString())
                .andExpect(jsonPath("$[0].src_acc_num").value("123456789"))
                .andExpect(jsonPath("$[0].dest_acc_num").isString())
                .andExpect(jsonPath("$[0].dest_acc_num").value("987654321"))
                .andExpect(jsonPath("$[0].amount").isNumber())
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[0].payer").exists())
                .andExpect(jsonPath("$[0].payer.first_name").isString())
                .andExpect(jsonPath("$[0].payer.first_name").value("Имя"))
                .andExpect(jsonPath("$[0].payer.last_name").isString())
                .andExpect(jsonPath("$[0].payer.last_name").value("Фамилия"))
                .andExpect(jsonPath("$[0].recipient").exists())
                .andExpect(jsonPath("$[0].recipient.first_name").isString())
                .andExpect(jsonPath("$[0].recipient.first_name").value("Имя"))
                .andExpect(jsonPath("$[0].recipient.last_name").isString())
                .andExpect(jsonPath("$[0].recipient.last_name").value("Фамилия"))
        ;
    }
}
