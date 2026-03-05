package com.vetcare.medical_record_service.controller;

import com.vetcare.medical_record_service.dtos.MedicalRecordDto;
import com.vetcare.medical_record_service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  @PostMapping
  public ResponseEntity<MedicalRecordDto> create(@RequestBody MedicalRecordDto recordDto) {
    return ResponseEntity.ok(medicalRecordService.createRecord(recordDto));
  }

  @GetMapping("/pet/{petId}")
  public ResponseEntity<List<MedicalRecordDto>> getByPet(@PathVariable Long petId) {
    return ResponseEntity.ok(medicalRecordService.getRecordsByPet(petId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MedicalRecordDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(medicalRecordService.getRecordById(id));
  }
}
