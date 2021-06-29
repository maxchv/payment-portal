package com.abank.base;

import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public abstract class AbstractClientCreateTest {

    @Autowired
    private MockMvc mockMvc;

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

    @SneakyThrows
    @Test
    @Order(2)
    void createClientInvalidFirstName() {
        var request = "{\n" +
                //"  \"first_name\": \"Имя\",\n" +
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
        mockMvc
                .perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].code").value("firstName"))
                .andExpect(jsonPath("$[0].text").exists());
    }

    @SneakyThrows
    @Test
    @Order(3)
    void createClientInvalidLastName() {
        var request = "{\n" +
                "  \"first_name\": \"Имя\",\n" +
//                "  \"last_name\": \"Фамилия\",\n" +
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
        mockMvc
                .perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].code").value("lastName"))
                .andExpect(jsonPath("$[0].text").exists());
    }

    @SneakyThrows
    @Test
    @Order(4)
    void createClientInvalidAccounts() {
        var request = "{\n" +
                "  \"first_name\": \"Имя\",\n" +
                "  \"last_name\": \"Фамилия\"" +
//                ",\n" +
//                "  \"accounts\": [\n" +
//                "    {\n" +
//                "      \"account_num\": \"123456789\",\n" +
//                "      \"account_type\": \"card/simple\",\n" +
//                "      \"balance\": 5000.00\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"account_num\": \"987654321\",\n" +
//                "      \"account_type\": \"card/simple\",\n" +
//                "      \"balance\": 10000.00\n" +
//                "    }\n" +
//                "  ]\n" +
                "}";
        mockMvc
                .perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].code").value("accounts"))
                .andExpect(jsonPath("$[0].text").exists());
    }
}
