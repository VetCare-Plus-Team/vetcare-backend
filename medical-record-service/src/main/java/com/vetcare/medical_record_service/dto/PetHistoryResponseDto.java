package com.vetcare.medical_record_service.dto;

import com.vetcare.medical_record_service.entity.MedicalRecord;
import lombok.Data;

import java.util.List;

@Data
public class PetHistoryResponseDto {
    private Object petDetails; // Profile data
    private List<MedicalRecord> medicalRecords; // Local DB data
    private List<Object> vaccinations; // From Vaccination Service
    private List<Object> medications; // From Medication Service
}
