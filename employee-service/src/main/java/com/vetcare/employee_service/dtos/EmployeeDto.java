package com.vetcare.employee_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String mobile;
  private String role;
  private String specialization;
  private String address;
  private String status;
}
