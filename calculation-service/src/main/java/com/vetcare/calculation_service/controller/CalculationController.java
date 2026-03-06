package com.vetcare.calculation_service.controller;


import com.vetcare.calculation_service.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/calculations")
@RequiredArgsConstructor
public class CalculationController {
    private final CalculationService calculationService;
    
    @GetMapping("/next-vaccine-date")
    public ResponseEntity<LocalDate> getNextDate(
            @RequestParam String vaccineName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate administeredDate) {
        
        return ResponseEntity.ok(calculationService.calculateNextVaccineDate(vaccineName, administeredDate));
    }
}
