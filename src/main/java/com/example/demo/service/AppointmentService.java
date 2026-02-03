package com.example.demo.service;

import com.example.demo.dto.CreateAppointmentRequest;
import com.example.demo.model.Appointment;
import com.example.demo.model.Client;
import com.example.demo.model.NailTech;
import com.example.demo.model.ServiceItem;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.NailTechRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final ClientRepository clientRepo;
    private final NailTechRepository techRepo;
    private final ServiceRepository serviceRepo;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository apptRepo,
                              ClientRepository clientRepo,
                              NailTechRepository techRepo,
                              ServiceRepository serviceRepo,
                              EmailService emailService) {

        this.apptRepo = apptRepo;
        this.clientRepo = clientRepo;
        this.techRepo = techRepo;
        this.serviceRepo = serviceRepo;
        this.emailService = emailService;
    }

    /* ----------------------------------------------------
     *                   CREATE
     * ---------------------------------------------------- */
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest req) {

        if (req.getClientName() == null || req.getClientName().isBlank()) {
            throw new RuntimeException("Client name is required");
        }

        // Buscar o crear client por nombre
        Client client = clientRepo.findByName(req.getClientName())
                .orElseGet(() -> {
                    Client c = new Client();
                    c.setName(req.getClientName());
                    // estos getters asumo que ya existen en tu DTO
                    c.setEmail(req.getClientEmail());
                    c.setPhone(req.getClientPhone());
                    return clientRepo.save(c);
                });

        // Si viene email desde el front, lo actualizamos
        if (req.getClientEmail() != null && !req.getClientEmail().isBlank()) {
            client.setEmail(req.getClientEmail());
            clientRepo.save(client);
        }

        // Nail tech (puede ser null)
        NailTech tech = null;
        if (req.getNailTechId() != null) {
            tech = techRepo.findById(req.getNailTechId())
                    .orElseThrow(() -> new RuntimeException("Nail tech not found"));
        }

        Appointment appt = new Appointment();
        appt.setClient(client);
        appt.setNailTech(tech);
        appt.setDate(LocalDate.parse(req.getDate()));
        appt.setStartTime(LocalTime.parse(req.getStartTime()));

        if (req.getEndTime() != null && !req.getEndTime().isBlank()) {
            appt.setEndTime(LocalTime.parse(req.getEndTime()));
        }

        Set<ServiceItem> services =
                new HashSet<>(serviceRepo.findAllById(req.getServiceIds()));
        appt.setServices(services);

        appt.setNotes(req.getNotes());

        Appointment saved = apptRepo.save(appt);

        // 👉 aquí se manda el correo usando SOLO EmailService
        emailService.sendAppointmentConfirmation(saved);

        return saved;
    }

    /* ----------------------------------------------------
     *                   UPDATE
     * ---------------------------------------------------- */
    @Transactional
    public Appointment updateAppointment(Long id, CreateAppointmentRequest req) {

        Appointment appt = apptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Client client = clientRepo.findByName(req.getClientName())
                .orElseGet(() -> {
                    Client c = new Client();
                    c.setName(req.getClientName());
                    c.setEmail(req.getClientEmail());
                    c.setPhone(req.getClientPhone());
                    return clientRepo.save(c);
                });

        if (req.getClientEmail() != null && !req.getClientEmail().isBlank()) {
            client.setEmail(req.getClientEmail());
            clientRepo.save(client);
        }

        appt.setClient(client);

        NailTech tech = null;
        if (req.getNailTechId() != null) {
            tech = techRepo.findById(req.getNailTechId())
                    .orElseThrow(() -> new RuntimeException("Nail technician not found"));
        }
        appt.setNailTech(tech);

        appt.setDate(LocalDate.parse(req.getDate()));
        appt.setStartTime(LocalTime.parse(req.getStartTime()));

        if (req.getEndTime() != null && !req.getEndTime().isBlank()) {
            appt.setEndTime(LocalTime.parse(req.getEndTime()));
        } else {
            appt.setEndTime(null);
        }

        appt.setNotes(req.getNotes());

        appt.getServices().clear();
        appt.getServices().addAll(serviceRepo.findAllById(req.getServiceIds()));

        return apptRepo.save(appt);
    }

    /* ----------------------------------------------------
     *                   DELETE
     * ---------------------------------------------------- */
    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appt = apptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        apptRepo.delete(appt);
    }

    /* ----------------------------------------------------
     *              GET APPOINTMENTS BY DATE
     *   (usado por tu ReminderScheduler para los recordatorios)
     * ---------------------------------------------------- */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return apptRepo.findByDate(date);
    }
}
