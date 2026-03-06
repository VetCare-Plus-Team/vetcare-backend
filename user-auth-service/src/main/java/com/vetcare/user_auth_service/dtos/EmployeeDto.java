package com.vetcare.user_auth_service.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String contact;
    private String address;
    private String designation;
    private String salaryDetails;
    private LocalDate joinedDate;
    private String profileImagePath;
}
