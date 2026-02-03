package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EnableScheduling
@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_tech_date", columnList = "nailtech_id,date")
        }
)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "nailtech_id")
    private NailTech nailTech;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "appointment_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceItem> services = new HashSet<>();

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(columnDefinition = "text")
    private String notes;

    // ⭐ NEW FIELD — completed
    @Column(nullable = false)
    private Boolean completed = false;

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public NailTech getNailTech() {
        return nailTech;
    }

    public void setNailTech(NailTech nailTech) {
        this.nailTech = nailTech;
    }

    public Set<ServiceItem> getServices() {
        return services;
    }

    public void setServices(Set<ServiceItem> services) {
        this.services = services;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ⭐ NEW GETTER/SETTER
    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
