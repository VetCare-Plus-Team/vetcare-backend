package com.vetcare.vaccination_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationDto {
  private Long id;
  private Long petId;
  private String vaccineName;
  private String batchNumber;
  private LocalDate vaccinationDate;
  private LocalDate nextDueDate;
  private Long adminBy;
}
