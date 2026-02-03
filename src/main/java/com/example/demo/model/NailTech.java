package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Entity
@Table(name = "nail_techs")
public class NailTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "nailtech_services",
        joinColumns = @JoinColumn(name = "nailtech_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceItem> services = new HashSet<>();

    @Column(columnDefinition = "text")
    private String availabilityJson;

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<ServiceItem> getServices() { return services; }
    public void setServices(Set<ServiceItem> services) { this.services = services; }

    public String getAvailabilityJson() { return availabilityJson; }
    public void setAvailabilityJson(String availabilityJson) { this.availabilityJson = availabilityJson; }
}
