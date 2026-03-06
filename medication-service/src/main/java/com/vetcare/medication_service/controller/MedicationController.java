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

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getMedicationsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getMedicationsByDoctor(doctorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable Long id, @RequestBody Medication medication) {
        return ResponseEntity.ok(service.updateMedication(id, medication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable Long id) {
        service.deleteMedication(id);
        return ResponseEntity.ok().build();
    }
}
