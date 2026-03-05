package com.vetcare.appointment_service.controller;

import com.vetcare.appointment_service.dtos.AppointmentDto;
import com.vetcare.appointment_service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;

  @PostMapping
  public ResponseEntity<AppointmentDto> create(@RequestBody AppointmentDto appointmentDto) {
    return ResponseEntity.ok(appointmentService.createAppointment(appointmentDto));
  }

  @GetMapping
  public ResponseEntity<List<AppointmentDto>> getAll() {
    return ResponseEntity.ok(appointmentService.getAllAppointments());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.getAppointmentById(id));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
    appointmentService.updateStatus(id, status);
    return ResponseEntity.ok().build();
  }
}
