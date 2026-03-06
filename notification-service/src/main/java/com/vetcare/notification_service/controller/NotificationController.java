package com.vetcare.notification_service.controller;


import com.vetcare.notification_service.dto.EmailRequestDto;
import com.vetcare.notification_service.scheduler.NotificationScheduler;
import com.vetcare.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationScheduler notificationScheduler;
    private final EmailService emailService;
    
    // 1. Manual trigger for Vaccination Reminders
    @PostMapping("/trigger-vaccine-reminders")
    public ResponseEntity<String> triggerVaccineReminders() {
        try {
            // Scheduler class eke thiyena method eka manually call karanawa
            notificationScheduler.sendVaccinationReminders();
            return ResponseEntity.ok("Vaccination reminders triggered successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to trigger reminders: " + e.getMessage());
        }
    }
    
    // 2. Generic endpoint to send an instant email
    @PostMapping("/send-email")
    public ResponseEntity<String> sendInstantEmail(@RequestBody EmailRequestDto request) {
        try {
            emailService.sendSimpleMail(request.getTo(), request.getSubject(), request.getMessage());
            return ResponseEntity.ok("Email sent successfully to " + request.getTo());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }
}
