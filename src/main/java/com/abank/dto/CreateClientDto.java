package com.abank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateClientDto {
    @JsonProperty("client_id")
    private Long clientId;
}
