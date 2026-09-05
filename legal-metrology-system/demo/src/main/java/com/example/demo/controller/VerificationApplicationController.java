package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.VerificationApplication;
import com.example.demo.repository.VerificationApplicationRepository;

@RestController
public class VerificationApplicationController {

    private final VerificationApplicationRepository repository;

    public VerificationApplicationController(
            VerificationApplicationRepository repository) {
        this.repository = repository;
    }

    // POST - Create verification application
    @PostMapping("/applications")
    public VerificationApplication createApplication(
            @RequestBody VerificationApplication application) {

        application.setStatus("PENDING");

        return repository.save(application);
    }

    // GET - View all applications
    @GetMapping("/applications")
    public List<VerificationApplication> getApplications() {
        return repository.findAll();
    }

    // GET - View one application by ID
    @GetMapping("/applications/{id}")
    public VerificationApplication getApplicationById(
            @PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    // PUT - Approve application
    @PutMapping("/applications/{id}/approve")
    public VerificationApplication approveApplication(
            @PathVariable Long id) {

        VerificationApplication application =
                repository.findById(id).orElse(null);

        if (application == null) {
            return null;
        }

        application.setStatus("APPROVED");

        return repository.save(application);
    }

    // PUT - Reject application
    @PutMapping("/applications/{id}/reject")
    public VerificationApplication rejectApplication(
            @PathVariable Long id) {

        VerificationApplication application =
                repository.findById(id).orElse(null);

        if (application == null) {
            return null;
        }

        application.setStatus("REJECTED");

        return repository.save(application);
    }
}