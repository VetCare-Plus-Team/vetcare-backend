package com.vetcare.user_auth_service.client;


import com.vetcare.user_auth_service.dtos.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    
    @PostMapping(value = "/api/employees/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> createEmployee(
            @RequestPart("employeeDetails") EmployeeDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file);
}
