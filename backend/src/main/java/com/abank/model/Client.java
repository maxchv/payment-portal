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
    @JsonProperty("client_id")
    @Column(name = "client_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    @NotNull
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonProperty("last_name")
    @NotNull
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty("accounts")
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
