package com.example.demo.repository;

import com.example.demo.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDate(LocalDate date);

    // Find appointments for a tech on a date that overlap a given time range
    @Query("SELECT a FROM Appointment a WHERE a.nailTech.id = :techId AND a.date = :date " +
           "AND ( (a.startTime < :endTime) AND (a.endTime > :startTime) )")
    List<Appointment> findOverlapping(
        @Param("techId") Long techId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
