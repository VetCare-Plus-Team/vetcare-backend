package com.vetcare.notification_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationDto {
    private Long id;
    private Long petId;
    private String vaccineName;
    private LocalDate dateAdministered;
    private LocalDate nextDueDate;
    private Long doctorId;
}
