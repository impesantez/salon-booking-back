package com.example.demo.controller;

import com.example.demo.model.ServiceItem;
import com.example.demo.repository.ServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")
public class ServiceController {

    private final ServiceRepository repo;

    public ServiceController(ServiceRepository repo) {
        this.repo = repo;
    }

    // ✅ Crear servicio
    @PostMapping
    public ServiceItem create(@RequestBody ServiceItem item) {
        return repo.save(item);
    }

    // ✅ Obtener todos los servicios
    @GetMapping
    public List<ServiceItem> getAll() {
        return repo.findAll();
    }

    // ✅ Actualizar servicio existente
    @PutMapping("/{id}")
    public ResponseEntity<ServiceItem> update(@PathVariable Long id, @RequestBody ServiceItem updated) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setCategory(updated.getCategory());
                    existing.setPrice(updated.getPrice());
                    existing.setDescription(updated.getDescription());
                    existing.setDurationMinutes(updated.getDurationMinutes());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar servicio
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
