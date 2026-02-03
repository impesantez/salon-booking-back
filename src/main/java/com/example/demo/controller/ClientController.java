package com.example.demo.controller;


import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")
public class ClientController {
    private final ClientRepository clientRepo;
    public ClientController(ClientRepository clientRepo) { this.clientRepo = clientRepo; }

    @GetMapping
    public List<Client> list() { return clientRepo.findAll(); }

    @PostMapping
    public Client create(@RequestBody Client c) { return clientRepo.save(c); }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return clientRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
