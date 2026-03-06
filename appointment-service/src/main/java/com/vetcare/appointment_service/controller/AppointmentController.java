package com.vetcare.appointment_service.controller;

import com.vetcare.appointment_service.dto.AppointmentRequestDto;
import com.vetcare.appointment_service.dto.RescheduleRequestDto;
import com.vetcare.appointment_service.enums.AppointmentStatus;
import com.vetcare.appointment_service.service.AppointmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentServiceImpl appointmentService;

    private boolean canManage(String role) {
        return role != null && (role.equals("ADMIN") || role.equals("STAFF"));
    }

    private boolean canView(String role) {
        return role != null && (role.equals("ADMIN") || role.equals("STAFF") || role.equals("DOCTOR"));
    }

    // --- Write Operations ---

    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody AppointmentRequestDto dto) {
        if (!canManage(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.bookAppointment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody AppointmentRequestDto dto) {
        if (!canManage(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody RescheduleRequestDto dto) {
        if (!canManage(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestParam AppointmentStatus status) {
        // DOCTOR tat status eka COMPLETED karanna access denna puluwan awashya nam.
        if (!canView(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!canManage(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully!");
    }

    // --- Read Operations (Calendar / List Views) ---

    @GetMapping
    public ResponseEntity<?> getAllAppointments(@RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!canView(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!canView(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!canView(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }
}
