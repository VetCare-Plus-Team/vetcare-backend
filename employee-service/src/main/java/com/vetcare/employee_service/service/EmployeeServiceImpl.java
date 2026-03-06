package com.vetcare.employee_service.service;

import com.vetcare.employee_service.dto.EmployeeDto;
import com.vetcare.employee_service.entity.Employee;
import com.vetcare.employee_service.exception.ResourceNotFoundException;
import com.vetcare.employee_service.file.FileService;
import com.vetcare.employee_service.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


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
    
    
    @Override
    public void updateEmployee(Long userId, EmployeeDto dto, MultipartFile file) throws IOException {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for User ID: " + userId));
    
        // Aluth photo ekak ewala nam witharak paran eka delete karala aluth eka danawa
        if (file != null && !file.isEmpty()) {
            if (employee.getProfileImagePath() != null) {
                fileService.deleteFile(imagePath, employee.getProfileImagePath());
            }
            String savedFileName = fileService.uploadFile(imagePath, file);
            employee.setProfileImagePath(savedFileName);
        }
    
        // Anith data tika update karanawa
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setContact(dto.getContact());
        employee.setAddress(dto.getAddress());
        employee.setDesignation(dto.getDesignation());
        employee.setSalaryDetails(dto.getSalaryDetails());
        employee.setJoinedDate(dto.getJoinedDate());
    
        employeeRepository.save(employee);
    }
    
    @Override
    public void deleteEmployee(Long userId) throws IOException {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for User ID: " + userId));
    
        // DB eken ain karanna kalin file eka makala danawa
        if (employee.getProfileImagePath() != null) {
            fileService.deleteFile(imagePath, employee.getProfileImagePath());
        }
    
        employeeRepository.delete(employee);
    }
    
    @Override
    public List<EmployeeDto> getEmployeesByUserIds(List<Long> userIds) {
        List<Employee> employees = employeeRepository.findByUserIdIn(userIds);
    
        // Entity list eka DTO list ekak karanawa
        return employees.stream().map(emp -> {
            EmployeeDto dto = new EmployeeDto();
            dto.setUserId(emp.getUserId());
            dto.setFirstName(emp.getFirstName());
            dto.setLastName(emp.getLastName());
            dto.setContact(emp.getContact());
            dto.setAddress(emp.getAddress());
            dto.setDesignation(emp.getDesignation());
            dto.setSalaryDetails(emp.getSalaryDetails());
            dto.setJoinedDate(emp.getJoinedDate());
            dto.setProfileImagePath(emp.getProfileImagePath()); // Image path eka set karanawa
            return dto;
        }).toList();
    }
    
    
}
