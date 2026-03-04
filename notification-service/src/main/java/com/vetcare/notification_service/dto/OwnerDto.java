package com.vetcare.notification_service.dto;

import lombok.Data;

@Data
public class OwnerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String contact;
    private String email;
    private String address;
}
