package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.VerificationSchedule;
import com.example.demo.repository.VerificationScheduleRepository;

@RestController
public class VerificationScheduleController {

    private final VerificationScheduleRepository repository;

    public VerificationScheduleController(
            VerificationScheduleRepository repository) {
        this.repository = repository;
    }

    // POST - Create schedule
    @PostMapping("/schedules")
    public VerificationSchedule createSchedule(
            @RequestBody VerificationSchedule schedule) {

        return repository.save(schedule);
    }

    // GET - View all schedules
    @GetMapping("/schedules")
    public List<VerificationSchedule> getSchedules() {

        return repository.findAll();
    }

    // GET - View one schedule
    @GetMapping("/schedules/{id}")
    public VerificationSchedule getScheduleById(
            @PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }
}