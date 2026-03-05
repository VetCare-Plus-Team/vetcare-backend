package com.vetcare.pet_profile_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
  private Long id;
  private String name;
  private String species;
  private String breed;
  private LocalDate dateOfBirth;
  private String gender;
  private String color;
  private Long ownerId;
}
