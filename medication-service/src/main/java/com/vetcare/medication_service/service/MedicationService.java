package com.vetcare.medication_service.service;

import com.vetcare.medication_service.config.RabbitMQConfig;
import com.vetcare.medication_service.dto.LogMessageDto;
import com.vetcare.medication_service.entity.Medication;
import com.vetcare.medication_service.repo.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository repo;
    private final RabbitTemplate rabbitTemplate;

    public Medication addMedication(Medication medication) {
        Medication saved = repo.save(medication);
        sendLog("MEDICATION_ADDED", saved.getPetId());
        return saved;
    }

    public Medication updateMedication(Long id, Medication details) {
        Medication medication = repo.findById(id).orElseThrow(() -> new RuntimeException("Medication not found"));
        medication.setMedicineName(details.getMedicineName());
        medication.setDosage(details.getDosage());
        medication.setFrequency(details.getFrequency());
        medication.setDuration(details.getDuration());
        medication.setInstructions(details.getInstructions());
        medication.setStatus(details.getStatus());
        medication.setStartDate(details.getStartDate());
        medication.setEndDate(details.getEndDate());
        Medication saved = repo.save(medication);
        sendLog("MEDICATION_UPDATED", saved.getPetId());
        return saved;
    }

    public void deleteMedication(Long id) {
        Medication medication = repo.findById(id).orElseThrow(() -> new RuntimeException("Medication not found"));
        repo.deleteById(id);
        sendLog("MEDICATION_DELETED", medication.getPetId());
    }

    public List<Medication> getMedicationByPetId(Long petId) {
        return repo.findByPetId(petId);
    }

    public List<Medication> getMedicationsByDoctor(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    private void sendLog(String action, Long targetId) {
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(targetId)
                    .action(action)
                    .serviceName("medication-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("RabbitMQ Log Failed: " + e.getMessage());
        }
    }
}
