package com.abank.repository.springdata;

import com.abank.model.Client;
import com.abank.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("springdata")
public interface SpringDataClientRepository extends JpaRepository<Client, Long>, ClientRepository {

}
