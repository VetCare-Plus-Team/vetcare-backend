package com.vetcare.user_auth_service.controller;


import com.vetcare.user_auth_service.dtos.*;
import com.vetcare.user_auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(
            @RequestPart("user") UserRegisterDto userDto,
            @RequestPart("employee") EmployeeDto employeeDto,
            @RequestPart(value = "file", required = false) MultipartFile file)
    {
       try{
           System.out.println(userDto);
           System.out.println(employeeDto);
           System.out.println(file);
           return ResponseEntity.ok(authService.registerFullUser(userDto, employeeDto, file));
       }catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Registration failed: " + e.getMessage());
       }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        
        try {
            return ResponseEntity.ok(authService.login(request));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login Faild: " + e.getMessage());
        }
        
    }
    
    
    
    @PutMapping(value = "/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestPart("user") UserRegisterDto userDto,
            @RequestPart("employee") EmployeeDto employeeDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        
        return ResponseEntity.ok(authService.updateUser(userId, userDto, employeeDto, file));
    }
    
    
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.deleteUser(userId));
    }
    
    // Get users details by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserEmployeeResponseDto>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(authService.getUsersByRole(role));
    }
    
}
