package com.vetcare.appointment_service.entity;


import com.vetcare.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long petId; // Pet Profile Service eke thiyena Pet ge ID eka
    private Long doctorId; // Employee Service eke thiyena Doctor ge ID eka
    
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    
    private String reason; // E.g., "Vaccination", "Checkup"
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
