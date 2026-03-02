package com.vetcare.employee_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // user id from Auth service
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    private String firstName;
    private String lastName;
    private String contact;
    private String address;
    private String designation; // e.g., DOCTOR, STAFF, ADMIN
    private String salaryDetails;
    private LocalDate joinedDate;
    
    @Column(name = "profile_image_path")
    private String profileImagePath;
    
}
