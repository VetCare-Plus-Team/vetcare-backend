package com.vetcare.notification_service.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetcare.notification_service.client.PetClient;
import com.vetcare.notification_service.client.VaccinationClient;
import com.vetcare.notification_service.dto.PetResponseDto;
import com.vetcare.notification_service.dto.VaccinationDto;
import com.vetcare.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    
    private final VaccinationClient vaccinationClient;
    private final PetClient petClient;
    private final EmailService emailService;
    
    // Dawasapatha ude 8:00 AM ta run wenna (Test karaddi "0 * * * * ?" danna)
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendVaccinationReminders() {
        System.out.println("--- Starting Vaccination Reminder Job ---");
        
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(3); // Thawa dawas 3kin due wena ewa
        
        // 1. DTO ekenma direct data gannawa
        List<VaccinationDto> upcomingVaccines = vaccinationClient.getUpcomingVaccines(today.toString(), targetDate.toString());
        
        for (VaccinationDto vaccine : upcomingVaccines) {
            try {
                // 2. Pet details direct DTO ekata gannawa
                PetResponseDto pet = petClient.getPetDetails(vaccine.getPetId());
                
                if (pet != null && pet.getOwner() != null && pet.getOwner().getEmail() != null) {
                    String ownerEmail = pet.getOwner().getEmail();
                    String ownerName = pet.getOwner().getFirstName();
                    String petName = pet.getName();
                    String vaccineName = vaccine.getVaccineName();
                    String dueDate = vaccine.getNextDueDate().toString();
                    
                    // 3. Email eka yawanawa
                    String subject = "VetCare Vaccination Reminder for " + petName;
                    String message = "Dear " + ownerName + ",\n\n" +
                            "This is a gentle reminder that your pet " + petName +
                            " is due for the '" + vaccineName + "' vaccination on " + dueDate + ".\n\n" +
                            "Please book an appointment soon.\n\nThank you,\nVetCare Clinic";
                    
                    emailService.sendSimpleMail(ownerEmail, subject, message);
                    System.out.println("Reminder sent to: " + ownerEmail);
                }
                
            } catch (Exception e) {
                System.out.println("Error sending email for Pet ID " + vaccine.getPetId() + ": " + e.getMessage());
            }
        }
    }
    
    
}
