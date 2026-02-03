package com.example.demo.controller;

import com.example.demo.dto.CreateAppointmentRequest;
import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")
public class AppointmentController {

    private final AppointmentRepository appointmentRepo;
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentRepository appointmentRepo,
                                 AppointmentService appointmentService) {
        this.appointmentRepo = appointmentRepo;
        this.appointmentService = appointmentService;
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(appointmentRepo.findAll());
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAppointmentRequest req) {
        try {
            Appointment created = appointmentService.createAppointment(req);
            return ResponseEntity.ok(created);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating appointment: " + ex.getMessage());
        }
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateAppointmentRequest req) {
        try {
            Appointment updated = appointmentService.updateAppointment(id, req);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating appointment: " + ex.getMessage());
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok("Appointment deleted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting appointment: " + ex.getMessage());
        }
    }

    // ✅ NEW — TOGGLE COMPLETED
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> toggleComplete(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        try {
            Optional<Appointment> opt = appointmentRepo.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }

            Appointment appt = opt.get();

            boolean completed = payload.get("completed") != null &&
                                Boolean.parseBoolean(payload.get("completed").toString());

            appt.setCompleted(completed);
            appointmentRepo.save(appt);

            return ResponseEntity.ok(appt);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling completion: " + ex.getMessage());
        }
    }
}
