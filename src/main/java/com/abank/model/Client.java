package com.abank.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "clients")
@Entity
@Data
public class Client {
    @JsonIgnore
    @Column(name = "client_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty("accounts")
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, orphanRemoval = true)
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
