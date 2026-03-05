package com.vetcare.medical_record_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDto {
  private Long id;
  private Long petId;
  private Long doctorId;
  private String symptoms;
  private String diagnosis;
  private String treatment;
  private String notes;
  private LocalDateTime createdAt;
}
