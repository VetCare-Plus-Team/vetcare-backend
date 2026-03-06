package com.vetcare.vaccination_service.controller;

import com.vetcare.vaccination_service.entity.Vaccination;
import com.vetcare.vaccination_service.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {
    private final VaccinationService service;

    @PostMapping
    public ResponseEntity<?> addVaccine(@RequestBody Vaccination vaccination) {
        return ResponseEntity.ok(service.addVaccine(vaccination));
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getByPetId(@PathVariable Long petId) {
        return ResponseEntity.ok(service.getVaccinesByPetId(petId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingVaccines(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.ok(service.getUpcomingVaccines(start, end));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getVaccinationsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getVaccinationsByDoctor(doctorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVaccine(@PathVariable Long id, @RequestBody Vaccination vaccination) {
        return ResponseEntity.ok(service.updateVaccine(id, vaccination));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaccine(@PathVariable Long id) {
        service.deleteVaccine(id);
        return ResponseEntity.ok().build();
    }
}
