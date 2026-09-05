package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Inspection;
import com.example.demo.model.VerificationApplication;
import com.example.demo.repository.InspectionRepository;
import com.example.demo.repository.VerificationApplicationRepository;

@RestController
public class InspectionController {

    private final InspectionRepository repository;
    private final VerificationApplicationRepository applicationRepository;

    public InspectionController(
            InspectionRepository repository,
            VerificationApplicationRepository applicationRepository) {

        this.repository = repository;
        this.applicationRepository = applicationRepository;
    }

    @PostMapping("/inspections")
    public Inspection createInspection(
            @RequestBody Inspection inspection) {

        Inspection savedInspection =
                repository.save(inspection);

        VerificationApplication application =
                applicationRepository
                        .findById(inspection.getApplicationId())
                        .orElse(null);

        if (application != null) {

            if (inspection.getResult().equals("PASS")) {

                application.setStatus("READY_FOR_CERTIFICATE");

            } else if (inspection.getResult().equals("FAIL")) {

                application.setStatus("INSPECTION_FAILED");
            }

            applicationRepository.save(application);
        }

        return savedInspection;
    }

    @GetMapping("/inspections")
    public List<Inspection> getInspections() {

        return repository.findAll();
    }
}