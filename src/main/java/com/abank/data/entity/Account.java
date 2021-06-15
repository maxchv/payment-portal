package com.abank.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
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
    @Column(name = "account_num", columnDefinition = "char(9)", nullable = false, unique = true)
    private String accountNum;

    @JsonProperty("account_type")
    @Column(name = "account_type")
    private String accountType;

    @JsonProperty("balance")
    @Column(name = "balance")
    private BigDecimal balance; // FIXME: format

    @ManyToOne
    @JoinColumn(name = "client_fk")
    @JsonIgnore
    private Client client;
}
