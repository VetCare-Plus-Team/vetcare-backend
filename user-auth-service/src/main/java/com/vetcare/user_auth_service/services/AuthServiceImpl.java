package com.vetcare.user_auth_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vetcare.user_auth_service.client.EmployeeClient;
import com.vetcare.user_auth_service.dtos.AuthRequestDto;
import com.vetcare.user_auth_service.dtos.AuthResponseDto;
import com.vetcare.user_auth_service.dtos.EmployeeDto;
import com.vetcare.user_auth_service.dtos.UserRegisterDto;
import com.vetcare.user_auth_service.entity.User;
import com.vetcare.user_auth_service.repo.UserRepository;
import com.vetcare.user_auth_service.shared.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmployeeClient employeeClient;
    
    @Override
    public String registerFullUser(UserRegisterDto userDto, EmployeeDto employeeDto, MultipartFile file) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
    
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setStatus("ACTIVE");
    
        User savedUser = userRepository.save(user);
    
        try {
            // EmployeeDto eka JSON String ekak karanawa
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String employeeJson = objectMapper.writeValueAsString(employeeDto);
    
            // Feign Client haraha ID eka wenamama yawanawa
            employeeClient.createEmployee(savedUser.getId(), employeeJson, file); // <-- Methana wenas wuna
        } catch (Exception e) {
            // Employee eka save une nathnam User wa makala danawa
            userRepository.delete(savedUser);
            throw new RuntimeException("Failed to save Employee details. User registration rolled back. Error: " + e.getMessage());
        }
    
        return "Registration Successful!";
    }
    
    
    
    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));
    
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid Password!");
        }
    
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId().toString());
        return new AuthResponseDto(token, user.getUsername(), user.getRole());
    }
    
    
}
