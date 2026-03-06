package com.vetcare.vaccination_service.service;

import com.vetcare.vaccination_service.client.CalculationClient;
import com.vetcare.vaccination_service.config.RabbitMQConfig;
import com.vetcare.vaccination_service.dto.LogMessageDto;
import com.vetcare.vaccination_service.entity.Vaccination;
import com.vetcare.vaccination_service.repo.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinationService {
    private final VaccinationRepository repo;
    private final RabbitTemplate rabbitTemplate;
    private final CalculationClient calculationClient;

    public Vaccination addVaccine(Vaccination vaccination) {
        // Next date eka dila nattam auto calculate karanawa
        if (vaccination.getNextDueDate() == null) {
            LocalDate nextDate = calculationClient.getNextDate(
                    vaccination.getVaccineName(),
                    vaccination.getDateAdministered());
            vaccination.setNextDueDate(nextDate);
        }
        Vaccination saved = repo.save(vaccination);
        sendLog("VACCINE_ADDED", saved.getPetId());
        return saved;
    }

    public List<Vaccination> getVaccinesByPetId(Long petId) {
        return repo.findByPetId(petId);
    }

    public Vaccination updateVaccine(Long id, Vaccination details) {
        Vaccination vaccination = repo.findById(id).orElseThrow(() -> new RuntimeException("Vaccination not found"));
        vaccination.setVaccineName(details.getVaccineName());
        vaccination.setType(details.getType());
        vaccination.setDose(details.getDose());
        vaccination.setDateAdministered(details.getDateAdministered());
        vaccination.setNextDueDate(details.getNextDueDate());

        // Recalculate if next date is still null
        if (vaccination.getNextDueDate() == null) {
            LocalDate nextDate = calculationClient.getNextDate(
                    vaccination.getVaccineName(),
                    vaccination.getDateAdministered());
            vaccination.setNextDueDate(nextDate);
        }

        Vaccination saved = repo.save(vaccination);
        sendLog("VACCINE_UPDATED", saved.getPetId());
        return saved;
    }

    public void deleteVaccine(Long id) {
        Vaccination vaccination = repo.findById(id).orElseThrow(() -> new RuntimeException("Vaccination not found"));
        repo.deleteById(id);
        sendLog("VACCINE_DELETED", vaccination.getPetId());
    }

    private void sendLog(String action, Long targetId) {
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(targetId)
                    .action(action)
                    .serviceName("vaccination-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("RabbitMQ Log Failed: " + e.getMessage());
        }
    }

    public List<Vaccination> getUpcomingVaccines(LocalDate startDate, LocalDate endDate) {
        return repo.findByNextDueDateBetween(startDate, endDate);
    }

    public List<Vaccination> getVaccinationsByDoctor(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }
}
