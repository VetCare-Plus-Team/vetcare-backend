package com.vetcare.user_auth_service.controller;

import com.vetcare.user_auth_service.dtos.AuthRequestDto;
import com.vetcare.user_auth_service.dtos.AuthResponseDto;
import com.vetcare.user_auth_service.dtos.UserRegisterDto;
import com.vetcare.user_auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody UserRegisterDto registerDto) {
    authService.register(registerDto);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequest) {
    return ResponseEntity.ok(authService.login(authRequest));
  }

  @GetMapping("/validate")
  public ResponseEntity<String> validate() {
    return ResponseEntity.ok("Token is valid");
  }
}
