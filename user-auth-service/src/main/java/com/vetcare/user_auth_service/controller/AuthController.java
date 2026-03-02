package com.vetcare.user_auth_service.controller;


import com.vetcare.user_auth_service.dtos.AuthRequestDto;
import com.vetcare.user_auth_service.dtos.AuthResponseDto;
import com.vetcare.user_auth_service.dtos.EmployeeDto;
import com.vetcare.user_auth_service.dtos.UserRegisterDto;
import com.vetcare.user_auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        System.out.println(userDto);
        System.out.println(employeeDto);
        System.out.println(file);
        return ResponseEntity.ok(authService.registerFullUser(userDto, employeeDto, file));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
