package com.connect.ClientMgmtREST.controller;

import com.connect.ClientMgmtREST.model.Client;
import com.connect.ClientMgmtREST.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() {
        ResponseEntity<String> response = clientController.index();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Welcome to the Client Management system", response.getBody());
    }

    @Test
    public void testGetImageByClientId() {
        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setImageData(new byte[]{1, 2, 3});

        when(clientService.getClientById(anyInt())).thenReturn(mockClient);

        ResponseEntity<byte[]> response = clientController.getImageByClientId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new byte[]{1, 2, 3}, response.getBody());
    }

    @Test
    public void testGetImageByClientIdNotFound() {
        when(clientService.getClientById(anyInt())).thenReturn(new Client(-1));

        ResponseEntity<byte[]> response = clientController.getImageByClientId(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetClients() {
        Client client1 = new Client();
        client1.setFirstName("Elon");

        Client client2 = new Client();
        client2.setFirstName("Mark");

        List<Client> mockClients = Arrays.asList(client1, client2);

        when(clientService.getAllClients()).thenReturn(mockClients);

        ResponseEntity<List<Client>> response = clientController.getClients();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Elon", response.getBody().get(0).getFirstName());
        assertEquals("Mark", response.getBody().get(1).getFirstName());
    }

    @Test
    public void testViewClientById() {
        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setFirstName("John");

        when(clientService.getClientById(anyInt())).thenReturn(mockClient);

        ResponseEntity<Client> response = clientController.viewClientById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    public void testViewClientByIdNotFound() {
        when(clientService.getClientById(anyInt())).thenReturn(new Client(-1));

        ResponseEntity<Client> response = clientController.viewClientById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateClient() throws IOException {
        Client mockClient = new Client();
        mockClient.setFirstName("Jane");

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        when(mockImage.getOriginalFilename()).thenReturn("image.jpg");
        when(mockImage.getContentType()).thenReturn("image/jpeg");
        when(mockImage.getBytes()).thenReturn(new byte[]{1, 2, 3});

        when(clientService.saveClient(any(Client.class), any(MultipartFile.class))).thenReturn(mockClient);

        ResponseEntity<?> response = clientController.createClient(mockClient, mockImage);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Jane", ((Client) response.getBody()).getFirstName());
    }

    @Test
    public void testEditClient() throws IOException {
        Client mockClient = new Client();
        mockClient.setFirstName("Jane");

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        when(mockImage.getOriginalFilename()).thenReturn("image.jpg");
        when(mockImage.getContentType()).thenReturn("image/jpeg");
        when(mockImage.getBytes()).thenReturn(new byte[]{1, 2, 3});

        when(clientService.updateClient(any(Client.class), anyInt(), any(MultipartFile.class))).thenReturn(mockClient);

        ResponseEntity<?> response = clientController.editClient(mockClient, 1, mockImage);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jane", ((Client) response.getBody()).getFirstName());
    }

    @Test
    public void testDeleteClient() {
        Client mockClient = new Client();
        mockClient.setId(1);

        when(clientService.getClientById(anyInt())).thenReturn(mockClient);

        ResponseEntity<String> response = clientController.deleteClient(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteClientNotFound() {
        when(clientService.getClientById(anyInt())).thenReturn(null);

        ResponseEntity<String> response = clientController.deleteClient(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
