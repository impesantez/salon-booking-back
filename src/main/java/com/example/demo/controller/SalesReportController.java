package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.SalesReport;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")
public class SalesReportController {

    private final AppointmentRepository appointmentRepository;

    public SalesReportController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/daily-sales")
    public List<SalesReport> getDailySales() {
        LocalDate today = LocalDate.now();

        // ✅ Filtra las citas del día actual
        List<Appointment> appointments = appointmentRepository.findAll().stream()
                .filter(a -> a.getDate() != null && a.getDate().equals(today))
                .collect(Collectors.toList());

        // ✅ Agrupa por Nail Tech
        Map<String, List<Appointment>> grouped = appointments.stream()
                .filter(a -> a.getNailTech() != null && a.getServices() != null)
                .collect(Collectors.groupingBy(a -> a.getNailTech().getName()));

        // ✅ Calcula totales
        List<SalesReport> result = new ArrayList<>();
        grouped.forEach((tech, apps) -> {
            double total = apps.stream()
                    .flatMap(a -> a.getServices().stream())
                    .mapToDouble(s -> Optional.ofNullable(s.getPrice()).orElse(0.0))
                    .sum();

            result.add(new SalesReport(tech, total, (long) apps.size()));
        });

        // ✅ Ordena por nombre
        result.sort(Comparator.comparing(SalesReport::getNailTech));
        return result;
    }
}
