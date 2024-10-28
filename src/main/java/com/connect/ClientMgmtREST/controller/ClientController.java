package com.connect.ClientMgmtREST.controller;

import com.connect.ClientMgmtREST.model.Client;
import com.connect.ClientMgmtREST.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Welcome to the Client Management system");
    }

    @GetMapping("/image/{clientId}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageByClientId(@PathVariable int clientId) {
        Client client = clientService.getClientById(clientId);
        if (client.getId() > 0)
            return new ResponseEntity<>(client.getImageData(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/allClients")
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // View client details by id
    @GetMapping("/view/{id}")
    public ResponseEntity<Client> viewClientById(@PathVariable("id") int id) {
        Client client = clientService.getClientById(id);
        if (client.getId() > 0) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClient(@RequestPart Client client, @RequestPart MultipartFile imageFile) throws IOException {
        System.out.println("Client: " + client);
        System.out.println("Email: " + client.getEmail());
        Client savedClient = null;
        try {
            savedClient = clientService.saveClient(client, imageFile);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editClient(@ModelAttribute Client client, @PathVariable Integer id, @RequestParam MultipartFile image) throws IOException {
        Client updatedClient = null;
        try {
            updatedClient = clientService.updateClient(client, id, image);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        Client client = clientService.getClientById(id);
        if (client != null){
            clientService.deleteClient(id);
            return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

