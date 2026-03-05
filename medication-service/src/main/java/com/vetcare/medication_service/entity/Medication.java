package com.vetcare.medication_service.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Data
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long petId;
    private String medicineName;
    private String dosage; // e.g., "1 pill"
    private String frequency; // e.g., "Twice a day"
    private LocalDate startDate;
    private LocalDate endDate;
    private Long doctorId;
}
