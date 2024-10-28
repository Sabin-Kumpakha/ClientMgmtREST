package com.connect.ClientMgmtREST.repository;

import com.connect.ClientMgmtREST.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    public Client findByEmail(String email);

    public Client findByPhoneNumber(String phoneNumber);

}
