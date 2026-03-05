package com.vetcare.qr_code_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Long appointmentId;
    private Long petId;
    private String petName;
    private String ownerName;
    private String veterinarianName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String clinicName;
    private String clinicAddress;
    private String status;
    private String reason;
}
