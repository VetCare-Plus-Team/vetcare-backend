package com.vetcare.medication_service.controller;


import com.vetcare.medication_service.entity.Medication;
import com.vetcare.medication_service.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService service;
    
    @PostMapping
    public ResponseEntity<?> addMedication(@RequestBody Medication medication) {
        return ResponseEntity.ok(service.addMedication(medication));
    }
    
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getByPetId(@PathVariable Long petId) {
        return ResponseEntity.ok(service.getMedicationByPetId(petId));
    }
    
}
