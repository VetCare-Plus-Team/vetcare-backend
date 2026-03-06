package com.vetcare.pet_profile_service.dto;

import lombok.Data;

@Data
public class PetDto {
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private Long ownerId;
    private Double weight;
}
