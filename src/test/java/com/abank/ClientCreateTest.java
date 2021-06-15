package com.abank;

import com.abank.data.entity.Account;
import com.abank.data.entity.Client;
import com.abank.data.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @SneakyThrows
    @Test
    @Transactional
    @Order(1)
    void createClient() {
        var request = "{\n" +
                "  \"first_name\": \"Имя\",\n" +
                "  \"last_name\": \"Фамилия\",\n" +
                "  \"accounts\": [\n" +
                "    {\n" +
                "      \"account_num\": \"123456789\",\n" +
                "      \"account_type\": \"card/simple\",\n" +
                "      \"balance\": 5000.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"account_num\": \"987654321\",\n" +
                "      \"account_type\": \"card/simple\",\n" +
                "      \"balance\": 10000.00\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Long expectedClientId = 1L;
        mockMvc
                .perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("client_id").value(expectedClientId));

        Client client = clientRepository.findById(expectedClientId).orElseThrow();
        assertEquals(expectedClientId, client.getId());
        assertEquals("Имя", client.getFirstName());
        assertEquals("Фамилия", client.getLastName());
        List<Account> accounts = client.getAccounts();
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertEquals("123456789", accounts.get(0).getAccountNum());
        assertEquals("card/simple", accounts.get(0).getAccountType());
        assertEquals(BigDecimal.valueOf(5000.00).stripTrailingZeros(), accounts.get(0).getBalance().stripTrailingZeros());
        assertEquals("987654321", accounts.get(1).getAccountNum());
        assertEquals("card/simple", accounts.get(1).getAccountType());
        assertEquals(BigDecimal.valueOf(10000.00).stripTrailingZeros(), accounts.get(1).getBalance().stripTrailingZeros());
    }
}
