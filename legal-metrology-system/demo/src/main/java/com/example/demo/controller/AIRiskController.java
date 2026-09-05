package com.example.demo.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Certificate;
import com.example.demo.model.VerificationApplication;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.VerificationApplicationRepository;

@RestController
public class AIRiskController {

    private final VerificationApplicationRepository applicationRepository;
    private final CertificateRepository certificateRepository;

    public AIRiskController(
            VerificationApplicationRepository applicationRepository,
            CertificateRepository certificateRepository) {

        this.applicationRepository = applicationRepository;
        this.certificateRepository = certificateRepository;
    }

    @GetMapping("/ai/test")
    public Map<String, Object> testAI() {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("message", "AI feature is working");
        return response;
    }

    @GetMapping("/ai/risk/{id}")
    public ResponseEntity<Map<String, Object>> calculateRisk(@PathVariable Long id) {

        VerificationApplication application =
                applicationRepository.findById(id).orElse(null);

        if (application == null) {

            Map<String, Object> notFound = new LinkedHashMap<>();
            notFound.put("error", "Application not found");
            notFound.put("applicationId", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
        }

        String status = application.getStatus();

        int riskScore = 0;


        // 1. Rejected application
        if (status.equals("REJECTED")) {

            riskScore += 70;
        }


        // 2. Inspection failed
        if (status.equals("INSPECTION_FAILED")) {

            riskScore += 70;
        }


        // 3. Pending application
        if (status.equals("PENDING")) {

            riskScore += 30;
        }


        // 4. Check certificate expiry
        List<Certificate> certificates =
                certificateRepository.findAll();

        for (Certificate certificate : certificates) {

            if (certificate.getApplicationId().equals(id)) {

                LocalDate today =
                        LocalDate.now();

                LocalDate validUntil =
                        LocalDate.parse(
                                certificate.getValidUntil());

                long daysLeft =
                        ChronoUnit.DAYS.between(
                                today,
                                validUntil);


                // Certificate expired
                if (daysLeft < 0) {

                    riskScore += 70;
                }

                // Certificate expires within 30 days
                else if (daysLeft <= 30) {

                    riskScore += 40;
                }

                // Certificate expires within 90 days
                else if (daysLeft <= 90) {

                    riskScore += 20;
                }

                break;
            }
        }


        // Maximum risk score is 100
        if (riskScore > 100) {

            riskScore = 100;
        }


        // Determine risk level
        String riskLevel;

        if (riskScore >= 70) {

            riskLevel = "HIGH";
        }

        else if (riskScore >= 30) {

            riskLevel = "MEDIUM";
        }

        else {

            riskLevel = "LOW";
        }


        Map<String, Object> result = new LinkedHashMap<>();
        result.put("applicationId", id);
        result.put("riskScore", riskScore);
        result.put("riskLevel", riskLevel);

        return ResponseEntity.ok(result);
    }
}