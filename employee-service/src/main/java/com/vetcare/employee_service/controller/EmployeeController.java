package com.vetcare.employee_service.controller;

import com.vetcare.employee_service.dtos.EmployeeDto;
import com.vetcare.employee_service.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @PostMapping
  public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto) {
    return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
  }

  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAll() {
    return ResponseEntity.ok(employeeService.getAllEmployees());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  @GetMapping("/role/{role}")
  public ResponseEntity<List<EmployeeDto>> getByRole(@PathVariable String role) {
    return ResponseEntity.ok(employeeService.getEmployeesByRole(role));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
    employeeService.updateStatus(id, status);
    return ResponseEntity.ok().build();
  }
}
