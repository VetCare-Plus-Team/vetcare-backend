package com.vetcare.notification_service.dto;


import lombok.Data;

@Data
public class PetResponseDto {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private String qrCodeFileName;
    private String qrCodeUrl;
    private OwnerDto owner;
}
