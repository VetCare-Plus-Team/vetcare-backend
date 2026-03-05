package com.vetcare.appointment_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
  private Long id;
  private Long petId;
  private Long doctorId;
  private LocalDateTime appointmentTime;
  private String status;
  private String reason;
}
