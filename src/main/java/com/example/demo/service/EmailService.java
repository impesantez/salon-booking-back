package com.example.demo.service;

import com.example.demo.model.Appointment;
import com.example.demo.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.replyTo:${app.mail.from}}")
    private String replyToAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ========= CONFIRMACIÓN =========
    public void sendAppointmentConfirmation(Appointment appointment) {
        Client client = appointment.getClient();
        if (client == null || client.getEmail() == null || client.getEmail().isBlank()) {
            return; // no hay email → no se manda nada
        }

        String to = client.getEmail();
        String subject = "Your appointment is confirmed - Get Nail'd LA";

        String text = buildConfirmationBody(appointment);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setReplyTo(replyToAddress);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // ========= RECORDATORIO =========
    public void sendAppointmentReminder(Appointment appointment) {
        Client client = appointment.getClient();
        if (client == null || client.getEmail() == null || client.getEmail().isBlank()) {
            return;
        }

        String to = client.getEmail();
        String subject = "Reminder: your appointment is tomorrow - Get Nail'd LA";

        String text = buildReminderBody(appointment);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setReplyTo(replyToAddress);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // ================== PLANTILLAS BÁSICAS ==================
    private String buildConfirmationBody(Appointment appointment) {
        Client client = appointment.getClient();

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

        String dateStr = appointment.getDate() != null ? appointment.getDate().format(dateFmt) : "";
        String timeStr = "";
        if (appointment.getStartTime() != null) {
            timeStr = appointment.getStartTime().format(timeFmt);
            if (appointment.getEndTime() != null) {
                timeStr += " - " + appointment.getEndTime().format(timeFmt);
            }
        }

        String tech = appointment.getNailTech() != null ? appointment.getNailTech().getName() : "Assigned technician";
        String clientName = client != null ? client.getName() : "Client";

        return """
               Hello %s,

               Your appointment at Get Nail'd LA has been confirmed. ✨

               Date: %s
               Time: %s
               Technician: %s

               If you need to reschedule, please contact us at least 24 hours in advance. There are no late cancellation fees.

               See you soon,
               Get Nail'd LA
               
                10864 La Grange Avenue
        		Los Angeles, CA 90025

        		(323) 629-7577
               """.formatted(clientName, dateStr, timeStr, tech);
    }

    private String buildReminderBody(Appointment appointment) {
        Client client = appointment.getClient();

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

        String dateStr = appointment.getDate() != null ? appointment.getDate().format(dateFmt) : "";
        String timeStr = "";
        if (appointment.getStartTime() != null) {
            timeStr = appointment.getStartTime().format(timeFmt);
            if (appointment.getEndTime() != null) {
                timeStr += " - " + appointment.getEndTime().format(timeFmt);
            }
        }

        String tech = appointment.getNailTech() != null ? appointment.getNailTech().getName() : "Your technician";
        String clientName = client != null ? client.getName() : "Client";

        return """
               Hello %s,

               This is a friendly reminder of your appointment at Get Nail'd LA tomorrow. ✨

               Date: %s
               Time: %s
               Technician: %s

               Let us know via telephone if you need to cancel or reschedule. We look forward to seeing you! 

               - Get Nail'd LA

        		10864 La Grange Avenue
        		Los Angeles, CA 90025

        		(323) 629-7577
               """.formatted(clientName, dateStr, timeStr, tech);
    }
}
