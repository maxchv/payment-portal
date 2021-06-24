package com.abank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ToString(exclude = "client")
@Entity
@Table(name = "accounts")
public class Account {
    @JsonProperty("account_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @JsonProperty("account_num")
    @Column(name = "account_num", length = 9, nullable = false, unique = true)
    @NotBlank
    @NotNull
    private String accountNum;

    @JsonProperty("account_type")
    @Column(name = "account_type", nullable = false)
    @NotBlank
    @NotNull
    private String accountType;

    @JsonProperty("balance")
    @Column(name = "balance")
    @Positive
    private BigDecimal balance; // FIXME: format

    @ManyToOne
    @JoinColumn(name = "client_fk")
    @JsonIgnore
    private Client client;
}
