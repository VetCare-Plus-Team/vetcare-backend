package com.vetcare.employee_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vetcare.employee_service.dto.EmployeeDto;
import com.vetcare.employee_service.file.FileService;
import com.vetcare.employee_service.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final FileService fileService;

    @Value("${project.profile.path}")
    private String imagePath;

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

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateEmployee(
            @PathVariable("userId") Long userId,
            @RequestPart("employeeDetails") String employeeDetailsJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EmployeeDto dto = objectMapper.readValue(employeeDetailsJson, EmployeeDto.class);

        employeeService.updateEmployee(userId, dto, file);
        return ResponseEntity.ok("Employee Updated Successfully!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("userId") Long userId) throws IOException {
        employeeService.deleteEmployee(userId);
        return ResponseEntity.ok("Employee Deleted Successfully!");
    }

    // get user details by id ss
    // 1. Bulk Employees ganna endpoint eka (Auth service eken call karanne)
    @PostMapping("/bulk")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByUserIds(@RequestBody List<Long> userIds) {
        return ResponseEntity.ok(employeeService.getEmployeesByUserIds(userIds));
    }

    // 2. Image eka Frontend ekata pennana endpoint eka (Oya kalin dunnu monolithic
    // code eken gaththa eka)
    @GetMapping("/image/{fileName}")
    public void serveImage(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourcesFile(imagePath, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE); // PNG/JPG dekata wada karanawa
        StreamUtils.copy(resourceFile, response.getOutputStream());
    }

}
