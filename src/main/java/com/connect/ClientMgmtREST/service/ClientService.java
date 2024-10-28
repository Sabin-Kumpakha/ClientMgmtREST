package com.connect.ClientMgmtREST.service;

import com.connect.ClientMgmtREST.model.Client;
import com.connect.ClientMgmtREST.repository.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public List<Client> getAllClients(){
        return clientRepo.findAll(Sort.by(Sort.Direction.DESC, "id")); //
    }

    public Client getClientById(int id){
        return clientRepo.findById(id).orElse(new Client(-1));
    }

    public Client saveClient(Client client, MultipartFile image) throws IOException {
        client.setCreatedAt(new Date());
        client.setImageName(image.getOriginalFilename());
        client.setImageType(image.getContentType());
        client.setImageData(image.getBytes());
        return clientRepo.save(client);
    }

    public void deleteClient(Integer id){
        clientRepo.deleteById(id);
    }

    public Client updateClient(Client updatedClient, Integer id, MultipartFile image) throws IOException {
        Client existingClient = clientRepo.findById(id).orElseThrow(() -> new RuntimeException("Client not found with id: "+id));
        // Update the fields of the existing client with the new values
        existingClient.setFirstName(updatedClient.getFirstName());
        existingClient.setLastName(updatedClient.getLastName());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setPhoneNumber(updatedClient.getPhoneNumber());
        existingClient.setAddress(updatedClient.getAddress());
        existingClient.setStatus(updatedClient.getStatus());
        existingClient.setUpdatedAt(new Date());
        // Handle image update only if a new image is uploaded
        if (image != null && !image.isEmpty()) {
            existingClient.setImageName(image.getOriginalFilename());
            existingClient.setImageType(image.getContentType());
            existingClient.setImageData(image.getBytes());
        }
        clientRepo.save(existingClient);
        return existingClient;
    }

}
