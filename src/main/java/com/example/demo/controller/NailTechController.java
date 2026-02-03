package com.example.demo.controller;

import com.example.demo.model.NailTech;
import com.example.demo.model.ServiceItem;
import com.example.demo.repository.NailTechRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RestController
@RequestMapping("/api/nailtechs")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")
public class NailTechController {

    private final NailTechRepository repo;
    private final ServiceRepository serviceRepo;

    public NailTechController(NailTechRepository repo, ServiceRepository serviceRepo) {
        this.repo = repo;
        this.serviceRepo = serviceRepo;
    }

    // ✅ Obtener todos los técnicos
    @GetMapping
    public List<NailTech> list() {
        return repo.findAll();
    }

    // ✅ Crear nuevo técnico
    @PostMapping
    public NailTech create(@RequestBody NailTech t) {
        // Si vienen servicios (como {id:1})
        if (t.getServices() != null && !t.getServices().isEmpty()) {
            Set<Long> ids = new HashSet<>();
            t.getServices().forEach(s -> ids.add(s.getId()));
            Set<ServiceItem> fullServices = new HashSet<>(serviceRepo.findAllById(ids));
            t.setServices(fullServices);
        }
        return repo.save(t);
    }

    // ✅ Obtener técnico por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Actualizar técnico existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NailTech updated) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setPhone(updated.getPhone());
                    existing.setAvailabilityJson(updated.getAvailabilityJson());

                    if (updated.getServices() != null) {
                        Set<Long> ids = new HashSet<>();
                        updated.getServices().forEach(s -> ids.add(s.getId()));
                        Set<ServiceItem> services = new HashSet<>(serviceRepo.findAllById(ids));
                        existing.setServices(services);
                    }

                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar técnico
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Obtener servicios de una NailTech específica
    @GetMapping("/{id}/services")
    public ResponseEntity<?> getServicesByNailTech(@PathVariable Long id) {
        return repo.findById(id)
                .map(t -> ResponseEntity.ok(t.getServices()))
                .orElse(ResponseEntity.notFound().build());
    }
}
