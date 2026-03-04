package com.vetcare.medical_record_service.controller;


import com.vetcare.medical_record_service.entity.MedicalRecord;
import com.vetcare.medical_record_service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService service;
    
    @PostMapping
    public ResponseEntity<?> addRecord(@RequestBody MedicalRecord record) {
        return ResponseEntity.ok(service.addRecord(record));
    }
    
    // QR Code eken hari Pet ID eken hari katha karanna one Endpoint eka
    @GetMapping("/history/{petId}")
    public ResponseEntity<?> getPetHistory(@PathVariable Long petId) {
        return ResponseEntity.ok(service.getFullPetHistory(petId));
    }
}
