package com.vetcare.user_auth_service.client;


import com.vetcare.user_auth_service.dtos.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    
    @PostMapping(value = "/api/employees/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> createEmployee(
            @RequestParam("userId") Long userId,
            @RequestPart("employeeDetails") String employeeDetailsJson,
            @RequestPart(value = "file", required = false) MultipartFile file);
    
    @PutMapping(value = "/api/employees/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> updateEmployee(
            @PathVariable("userId") Long userId,
            @RequestPart("employeeDetails") String employeeDetailsJson,
            @RequestPart(value = "file", required = false) MultipartFile file);
    
    @DeleteMapping("/api/employees/{userId}")
    ResponseEntity<String> deleteEmployee(@PathVariable("userId") Long userId);
    
    
    // Get user details by id
    @PostMapping("/api/employees/bulk")
    List<EmployeeDto> getEmployeesByUserIds(@RequestBody List<Long> userIds);
}
