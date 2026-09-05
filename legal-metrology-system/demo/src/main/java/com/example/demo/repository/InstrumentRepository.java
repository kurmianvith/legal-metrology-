package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

}