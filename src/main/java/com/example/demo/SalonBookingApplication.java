package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SalonBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalonBookingApplication.class, args);
    }
}
