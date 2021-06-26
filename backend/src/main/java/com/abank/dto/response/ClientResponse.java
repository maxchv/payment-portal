package com.abank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class ClientResponse {
    @JsonProperty("client_id")
    @Positive
    private Long clientId;
}
