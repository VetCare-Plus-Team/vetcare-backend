package com.vetcare.employee_service.service;

import com.vetcare.employee_service.dto.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeDto dto, MultipartFile file) throws IOException;
    void updateEmployee(Long userId, EmployeeDto dto, MultipartFile file) throws IOException;
    void deleteEmployee(Long userId) throws IOException;
    
    List<EmployeeDto> getEmployeesByUserIds(List<Long> userIds);
}
