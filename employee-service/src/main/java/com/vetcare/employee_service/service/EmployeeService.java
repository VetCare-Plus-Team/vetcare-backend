package com.vetcare.employee_service.service;

import com.vetcare.employee_service.dto.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmployeeService {
    void createEmployee(EmployeeDto dto, MultipartFile file) throws IOException;
}
