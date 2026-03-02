package com.vetcare.employee_service.service;

import com.vetcare.employee_service.dto.EmployeeDto;
import com.vetcare.employee_service.entity.Employee;
import com.vetcare.employee_service.file.FileService;
import com.vetcare.employee_service.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    
    private final EmployeeRepository employeeRepository;
    private final FileService fileService;
    
    
    @Value("${project.profile.path}")
    private String imagePath;
    
    @Override
    public void createEmployee(EmployeeDto dto, MultipartFile file) throws IOException {
    
        String savedFileName = null;
        if (file != null && !file.isEmpty()) {
            savedFileName = fileService.uploadFile(imagePath, file);
        }
    
        Employee employee = new Employee();
        employee.setUserId(dto.getUserId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setContact(dto.getContact());
        employee.setAddress(dto.getAddress());
        employee.setDesignation(dto.getDesignation());
        employee.setSalaryDetails(dto.getSalaryDetails());
        employee.setJoinedDate(dto.getJoinedDate());
        employee.setProfileImagePath(savedFileName);
    
        employeeRepository.save(employee);
    }
}
