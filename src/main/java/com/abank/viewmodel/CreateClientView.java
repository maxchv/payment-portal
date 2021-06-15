package com.abank.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateClientView {
    @JsonProperty("client_id")
    private Long clientId;
}
