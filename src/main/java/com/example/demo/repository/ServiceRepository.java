package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ServiceItem;

public interface ServiceRepository extends JpaRepository<ServiceItem, Long> {
}
