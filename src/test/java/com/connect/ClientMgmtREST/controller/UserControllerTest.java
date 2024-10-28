package com.connect.ClientMgmtREST.controller;

import com.connect.ClientMgmtREST.model.Client;
import com.connect.ClientMgmtREST.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() {
        ResponseEntity<String> response = userController.index();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Welcome to the User Clients API", response.getBody());
    }

    @Test
    public void testGetClients() {
        Client client1 = new Client();
        client1.setFirstName("Elon");

        Client client2 = new Client();
        client2.setFirstName("Mark");

        List<Client> mockClients = Arrays.asList(client1, client2);

        when(clientService.getAllClients()).thenReturn(mockClients);

        ResponseEntity<List<Client>> response = userController.getClients();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Elon", response.getBody().get(0).getFirstName());
        assertEquals("Mark", response.getBody().get(1).getFirstName());
    }
}
