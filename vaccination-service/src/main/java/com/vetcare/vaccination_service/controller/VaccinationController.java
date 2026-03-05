package com.vetcare.vaccination_service.controller;

import com.vetcare.vaccination_service.dtos.VaccinationDto;
import com.vetcare.vaccination_service.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

  private final VaccinationService vaccinationService;

  @PostMapping
  public ResponseEntity<VaccinationDto> create(@RequestBody VaccinationDto vaccinationDto) {
    return ResponseEntity.ok(vaccinationService.createVaccination(vaccinationDto));
  }

  @GetMapping("/pet/{petId}")
  public ResponseEntity<List<VaccinationDto>> getByPet(@PathVariable Long petId) {
    return ResponseEntity.ok(vaccinationService.getVaccinationsByPet(petId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<VaccinationDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(vaccinationService.getVaccinationById(id));
  }
}
