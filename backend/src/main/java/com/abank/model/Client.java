package com.abank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "clients")
@Entity
@Data
public class Client {
    @Id
    @JsonProperty("client_id")
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty("accounts")
    @NotNull
    @OneToMany(mappedBy = "client")
    private List<Account> accounts;

    public void addAccount(Account account) {
        if(accounts == null) {
            accounts = new ArrayList<>();
        }
        account.setClient(this);
        accounts.add(account);
    }

    public void addAccounts(Collection<? extends Account> list) {
        if(accounts == null) {
            accounts = new ArrayList<>();
        }
        for (Account account: list) {
            account.setClient(this);
        }
        accounts.addAll(list);
    }
}
