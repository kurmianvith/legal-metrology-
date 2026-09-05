package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.VerificationSchedule;

public interface VerificationScheduleRepository
        extends JpaRepository<VerificationSchedule, Long> {

}