package com.vetcare.vaccination_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vaccinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccination {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long petId;

  @Column(nullable = false)
  private String vaccineName;

  private String batchNumber;
  private LocalDate vaccinationDate;
  private LocalDate nextDueDate;

  @Column(nullable = false)
  private Long adminBy; // Doctor ID

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
