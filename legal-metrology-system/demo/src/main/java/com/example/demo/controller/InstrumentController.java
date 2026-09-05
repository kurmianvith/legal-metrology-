package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;

@RestController
public class InstrumentController {

    private final InstrumentRepository repository;

    public InstrumentController(InstrumentRepository repository) {
        this.repository = repository;
    }

    // GET all instruments
    @GetMapping("/instruments")
    public List<Instrument> getInstruments() {
        return repository.findAll();
    }

    // GET one instrument by ID
    @GetMapping("/instruments/{id}")
    public Instrument getInstrumentById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // POST - Add instrument
    @PostMapping("/instruments")
    public Instrument addInstrument(@RequestBody Instrument instrument) {
        return repository.save(instrument);
    }

    // PUT - Update instrument
    @PutMapping("/instruments/{id}")
    public Instrument updateInstrument(@PathVariable Long id, @RequestBody Instrument instrument) {
        instrument.setId(id);
        return repository.save(instrument);
    }

    // DELETE - Delete instrument
    @DeleteMapping("/instruments/{id}")
    public String deleteInstrument(@PathVariable Long id) {
        repository.deleteById(id);
        return "Instrument deleted";
    }
}