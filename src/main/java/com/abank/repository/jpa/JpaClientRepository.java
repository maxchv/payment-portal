package com.abank.repository.jpa;

import com.abank.model.Client;

import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaClientRepository extends JpaRepository<Client, Long>, ClientRepository {

}
