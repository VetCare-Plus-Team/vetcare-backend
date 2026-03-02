package com.vetcare.employee_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vetcare.employee_service.dto.EmployeeDto;
import com.vetcare.employee_service.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    
    // This method call from Auth service
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createEmployee(
            @RequestParam("userId") Long userId,
            @RequestPart("employeeDetails") String employeeDetailsJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
    
        // 1. String eka aye EmployeeDto object ekak karanawa
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EmployeeDto dto = objectMapper.readValue(employeeDetailsJson, EmployeeDto.class);
    
        dto.setUserId(userId);
        // 2. Service ekata pass karanawa
        employeeService.createEmployee(dto, file);
        return ResponseEntity.ok("Employee Created Successfully!");
    }
    
}
