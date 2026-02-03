package com.example.demo.repository;

import com.example.demo.model.NailTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NailTechRepository extends JpaRepository<NailTech, Long> {
}
