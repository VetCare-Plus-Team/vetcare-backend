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
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestPart("user") UserRegisterDto userDto,
            @RequestPart("employee") EmployeeDto employeeDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        // Staff can also register users (like Doctors or other Staff)
        if (role == null || (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STAFF"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: You don't have permission to register users.");
        }

        try {
            return ResponseEntity.ok(authService.registerFullUser(userDto, employeeDto, file));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {

        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login Faild: " + e.getMessage());
        }

    }

    @PutMapping(value = "/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestPart("user") UserRegisterDto userDto,
            @RequestPart("employee") EmployeeDto employeeDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        if (role == null || (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STAFF"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: You don't have permission to update users.");
        }

        return ResponseEntity.ok(authService.updateUser(userId, userDto, employeeDto, file));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete employees.");
        }
        return ResponseEntity.ok(authService.deleteUser(userId));
    }

    // Get users details by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserEmployeeResponseDto>> getUsersByRole(@PathVariable("role") String role) {
        return ResponseEntity.ok(authService.getUsersByRole(role));
    }

}
