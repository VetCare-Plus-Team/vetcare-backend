package com.vetcare.vaccination_service.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "vaccinations")
@Data
public class Vaccination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long petId;
    private String vaccineName;
    private LocalDate dateAdministered;
    private LocalDate nextDueDate;
    private Long doctorId;
}
