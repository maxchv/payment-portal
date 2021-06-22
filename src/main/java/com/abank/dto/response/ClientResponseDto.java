package com.abank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientResponseDto {
    @JsonProperty("client_id")
    private Long clientId;
}
