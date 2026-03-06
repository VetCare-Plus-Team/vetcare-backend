package com.vetcare.medical_record_service.service;

import com.vetcare.medical_record_service.client.MedicationClient;
import com.vetcare.medical_record_service.client.PetClient;
import com.vetcare.medical_record_service.client.VaccinationClient;
import com.vetcare.medical_record_service.config.RabbitMQConfig;
import com.vetcare.medical_record_service.dto.LogMessageDto;
import com.vetcare.medical_record_service.dto.PetHistoryResponseDto;
import com.vetcare.medical_record_service.entity.MedicalRecord;
import com.vetcare.medical_record_service.repo.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository repo;
    private final PetClient petClient;
    private final VaccinationClient vaccinationClient;
    private final MedicationClient medicationClient;
    private final RabbitTemplate rabbitTemplate;

    public MedicalRecord addRecord(MedicalRecord record) {
        MedicalRecord saved = repo.save(record);
        sendLog("MEDICAL_RECORD_ADDED", saved.getPetId());
        return saved;
    }

    // AGGREGATOR METHOD (Call this method when scan QR)
    public PetHistoryResponseDto getFullPetHistory(Long petId) {
        PetHistoryResponseDto response = new PetHistoryResponseDto();

        // 1. Get Pet Profile (from pet-profile-service)
        try {
            response.setPetDetails(petClient.getPetDetails(petId));
        } catch (Exception e) {
            response.setPetDetails("Pet Profile not available or error fetching");
        }

        // 2. Get Medical Records (from Local DB)
        response.setMedicalRecords(repo.findByPetId(petId));

        // 3. Get Vaccinations (from vaccination-service)
        try {
            response.setVaccinations(vaccinationClient.getVaccinations(petId));
        } catch (Exception e) {
            response.setVaccinations(List.of("Error fetching vaccinations"));
        }

        // 4. Get Medications (from medication-service)
        try {
            response.setMedications(medicationClient.getMedications(petId));
        } catch (Exception e) {
            response.setMedications(List.of("Error fetching medications"));
        }

        return response;
    }

    public MedicalRecord updateRecord(Long id, MedicalRecord details) {
        MedicalRecord record = repo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        record.setDiagnosis(details.getDiagnosis());
        record.setSymptoms(details.getSymptoms());
        record.setTreatment(details.getTreatment());
        record.setVisitDate(details.getVisitDate());
        record.setFollowUpDate(details.getFollowUpDate());
        record.setNotes(details.getNotes());
        MedicalRecord saved = repo.save(record);
        sendLog("MEDICAL_RECORD_UPDATED", saved.getPetId());
        return saved;
    }

    public void deleteRecord(Long id) {
        MedicalRecord record = repo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        repo.deleteById(id);
        sendLog("MEDICAL_RECORD_DELETED", record.getPetId());
    }

    public List<MedicalRecord> getRecordsByDoctor(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    public List<MedicalRecord> getRecordsByPetId(Long petId) {
        return repo.findByPetId(petId);
    }

    public List<MedicalRecord> getAllRecords() {
        return repo.findAll();
    }

    private void sendLog(String action, Long targetId) {
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(targetId)
                    .action(action)
                    .serviceName("medical-record-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("RabbitMQ Log Failed: " + e.getMessage());
        }
    }
}
