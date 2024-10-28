package com.connect.ClientMgmtREST.service;

import com.connect.ClientMgmtREST.enums.Status;
import com.connect.ClientMgmtREST.model.Client;
import com.connect.ClientMgmtREST.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository); // Manually inject mock
    }

    @Test
    public void testGetAllClients() {
        Client client1 = new Client();
        client1.setFirstName("Elon");

        Client client2 = new Client();
        client2.setFirstName("Mark");

        List<Client> mockClients = Arrays.asList(client1, client2);

        Mockito.when(clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(mockClients);

        List<Client> clients = clientService.getAllClients();

        // Debugging output
        System.out.println("Number of clients: " + clients.size());
        clients.forEach(client -> System.out.println("Client name: " + client.getFirstName()));

        assertEquals(2, clients.size());
        assertEquals("Elon", clients.get(0).getFirstName());
        assertEquals("Mark", clients.get(1).getFirstName());
    }

    @Test
    public void testGetClientById() {
        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setFirstName("John");

        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(mockClient));

        Client client = clientService.getClientById(1);
        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals("John", client.getFirstName());
    }

    @Test
    public void testGetClientByIdNotFound() {
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.empty());

        Client client = clientService.getClientById(1);
        assertNotNull(client);
        assertEquals(-1, client.getId());
    }

    @Test
    public void testSaveClient() throws IOException {
        Client mockClient = new Client();
        mockClient.setFirstName("Bill");

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImage.getOriginalFilename()).thenReturn("image.jpg");
        Mockito.when(mockImage.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockImage.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(mockClient);

        Client savedClient = clientService.saveClient(mockClient, mockImage);
        assertNotNull(savedClient);
        assertEquals("Bill", savedClient.getFirstName());
        assertEquals("image.jpg", savedClient.getImageName());
        assertEquals("image/jpeg", savedClient.getImageType());
        assertArrayEquals(new byte[]{1, 2, 3}, savedClient.getImageData());
    }

    @Test
    public void testDeleteClient() {
        int clientId = 1;

        clientService.deleteClient(clientId);

        Mockito.verify(clientRepository, Mockito.times(1)).deleteById(clientId);
    }

    @Test
    public void testUpdateClient() throws IOException {
        Client existingClient = new Client();
        existingClient.setId(1);
        existingClient.setFirstName("Elon");

        Client updatedClient = new Client();
        updatedClient.setFirstName("Jeff");
        updatedClient.setLastName("Bezos");
        updatedClient.setEmail("bezos@gmail.com");
        updatedClient.setPhoneNumber("1234567890");
        updatedClient.setAddress("USA");
        updatedClient.setStatus(Status.New);

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImage.getOriginalFilename()).thenReturn("newimage.jpg");
        Mockito.when(mockImage.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockImage.getBytes()).thenReturn(new byte[]{4, 5, 6});

        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(existingClient));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(existingClient);

        Client result = clientService.updateClient(updatedClient, 1, mockImage);

        // Debugging output
        System.out.println("Updated Client ID: " + result.getId());
        System.out.println("Updated Client First Name: " + result.getFirstName());
        System.out.println("Updated Client Last Name: " + result.getLastName());
        System.out.println("Updated Client Email: " + result.getEmail());
        System.out.println("Updated Client Phone Number: " + result.getPhoneNumber());
        System.out.println("Updated Client Address: " + result.getAddress());
        System.out.println("Updated Client Status: " + result.getStatus());
        System.out.println("Updated Client Image Name: " + result.getImageName());
        System.out.println("Updated Client Image Type: " + result.getImageType());
        System.out.println("Updated Client Image Data: " + Arrays.toString(result.getImageData()));

        assertNotNull(result);
        assertEquals("Jeff", result.getFirstName());
        assertEquals("Bezos", result.getLastName());
        assertEquals("bezos@gmail.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("USA", result.getAddress());
        assertEquals(Status.New, result.getStatus());
        assertEquals("newimage.jpg", result.getImageName());
        assertEquals("image/jpeg", result.getImageType());
        assertArrayEquals(new byte[]{4, 5, 6}, result.getImageData());
    }

}
