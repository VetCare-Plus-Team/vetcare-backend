package com.vetcare.employee_service.service;

import com.vetcare.employee_service.dtos.EmployeeDto;
import com.vetcare.employee_service.entity.Employee;
import com.vetcare.employee_service.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ModelMapper modelMapper;

  public EmployeeDto createEmployee(EmployeeDto employeeDto) {
    Employee employee = modelMapper.map(employeeDto, Employee.class);
    employee.setStatus("ACTIVE");
    Employee saved = employeeRepository.save(employee);
    return modelMapper.map(saved, EmployeeDto.class);
  }

  public List<EmployeeDto> getAllEmployees() {
    return employeeRepository.findAll().stream()
        .map(employee -> modelMapper.map(employee, EmployeeDto.class))
        .collect(Collectors.toList());
  }

  public EmployeeDto getEmployeeById(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    return modelMapper.map(employee, EmployeeDto.class);
  }

  public List<EmployeeDto> getEmployeesByRole(String role) {
    return employeeRepository.findByRole(role).stream()
        .map(employee -> modelMapper.map(employee, EmployeeDto.class))
        .collect(Collectors.toList());
  }

  public void updateStatus(Long id, String status) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    employee.setStatus(status);
    employeeRepository.save(employee);
  }
}
