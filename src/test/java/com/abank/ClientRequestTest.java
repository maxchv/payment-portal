package com.abank;

import com.abank.client.account.Account;
import com.abank.client.Client;
import com.abank.client.repository.ClientRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientRequestTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ClientRepository mockClientRepository;

    @SneakyThrows
    @Test
    @Order(2)
    void findClient() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Имя");
        client.setFirstName("Фамилия");
        Account account1 = new Account();
        account1.setId(1L);
        account1.setAccountNum("123456789");
        account1.setAccountType("card/simple");
        account1.setBalance(BigDecimal.valueOf(5000));
        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNum("987654321");
        account2.setAccountType("card/simple");
        account2.setBalance(BigDecimal.valueOf(10000));
        client.addAccount(account1);
        client.addAccount(account2);

        Mockito.when(mockClientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        mockMvc
                .perform(
                        get("/api/v1/clients")
                                .queryParam("client_id", String.valueOf(1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].account_id").value("1"))
                .andExpect(jsonPath("$[0].account_num").value("123456789"))
                .andExpect(jsonPath("$[0].account_type").value("card/simple"))
                .andExpect(jsonPath("$[0].balance").value(5000))
                .andExpect(jsonPath("$[1].account_id").value("2"))
                .andExpect(jsonPath("$[1].account_num").value("987654321"))
                .andExpect(jsonPath("$[1].account_type").value("card/simple"))
                .andExpect(jsonPath("$[1].balance").value(10000));
    }
}
