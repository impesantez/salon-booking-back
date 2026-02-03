package com.example.demo.scheduler;

import com.example.demo.model.Appointment;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReminderScheduler {

    private final AppointmentService appointmentService;
    private final EmailService emailService;

    public ReminderScheduler(AppointmentService appointmentService, EmailService emailService) {
        this.appointmentService = appointmentService;
        this.emailService = emailService;
    }

    // Ejecutar todos los días a las 9AM
    @Scheduled(cron = "0 0 9 * * *", zone = "America/Los_Angeles")
    public void sendReminders() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> appts = appointmentService.getAppointmentsByDate(tomorrow);

        for (Appointment a : appts) {

            // Solo enviar si tiene email
            if (a.getClient() != null && a.getClient().getEmail() != null) {

                emailService.sendAppointmentReminder(a);

                System.out.println("📧 Reminder sent to " + a.getClient().getEmail());
            }
        }
    }
}
