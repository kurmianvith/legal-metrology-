package com.example.demo.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Certificate;
import com.example.demo.model.VerificationApplication;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.VerificationApplicationRepository;

@RestController
public class CertificateController {

    private final CertificateRepository repository;
    private final VerificationApplicationRepository applicationRepository;

    public CertificateController(
            CertificateRepository repository,
            VerificationApplicationRepository applicationRepository) {

        this.repository = repository;
        this.applicationRepository = applicationRepository;
    }

    // Create certificate
    @PostMapping("/certificates")
    public Certificate createCertificate(
            @RequestBody Certificate certificate) {

        Certificate savedCertificate =
                repository.save(certificate);

        VerificationApplication application =
                applicationRepository
                        .findById(certificate.getApplicationId())
                        .orElse(null);

        if (application != null) {

            application.setStatus("CERTIFICATE_ISSUED");

            applicationRepository.save(application);
        }

        return savedCertificate;
    }

    // Get all certificates
    @GetMapping("/certificates")
    public List<Certificate> getCertificates() {

        return repository.findAll();
    }

    // Get certificate by ID
    @GetMapping("/certificates/{id}")
    public Certificate getCertificateById(
            @PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    // Verify certificate
    @GetMapping("/certificates/verify/{certificateNumber}")
    public ResponseEntity<Map<String, Object>> verifyCertificate(
            @PathVariable String certificateNumber) {

        for (Certificate certificate : repository.findAll()) {

            if (certificate.getCertificateNumber()
                    .equals(certificateNumber)) {

                boolean isValid = certificate.getStatus().equals("VALID");

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("certificateNumber", certificateNumber);
                response.put("valid", isValid);
                response.put("status", certificate.getStatus());
                response.put("message",
                        isValid ? "Certificate is VALID" : "Certificate is NOT VALID");

                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> notFound = new LinkedHashMap<>();
        notFound.put("certificateNumber", certificateNumber);
        notFound.put("valid", false);
        notFound.put("message", "Certificate not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
    }

    // Get certificate details
    @GetMapping("/certificates/verify/details/{certificateNumber}")
    public Certificate getCertificateDetails(
            @PathVariable String certificateNumber) {

        for (Certificate certificate : repository.findAll()) {

            if (certificate.getCertificateNumber()
                    .equals(certificateNumber)) {

                return certificate;
            }
        }

        return null;
    }

    // Check certificate expiry
    @GetMapping("/certificates/expiry")
    public List<Certificate> checkExpiry() {

        LocalDate today = LocalDate.now();

        List<Certificate> certificates =
                repository.findAll();

        for (Certificate certificate : certificates) {

            LocalDate validUntil =
                    LocalDate.parse(certificate.getValidUntil());

            if (validUntil.isBefore(today)) {

                certificate.setStatus("EXPIRED");

            } else if (!validUntil.isAfter(today.plusDays(30))) {

                certificate.setStatus("EXPIRING_SOON");

            } else {

                certificate.setStatus("VALID");
            }

            repository.save(certificate);
        }

        return repository.findAll();
    }

    // Delete certificate
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<Map<String, Object>> deleteCertificate(
            @PathVariable Long id) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", id);

        if (!repository.existsById(id)) {
            response.put("message", "Certificate not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        repository.deleteById(id);

        response.put("message", "Certificate deleted");
        return ResponseEntity.ok(response);
    }
}