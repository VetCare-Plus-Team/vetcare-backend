package com.vetcare.user_auth_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vetcare.user_auth_service.client.EmployeeClient;
import com.vetcare.user_auth_service.config.RabbitMQConfig;
import com.vetcare.user_auth_service.dtos.*;
import com.vetcare.user_auth_service.entity.User;
import com.vetcare.user_auth_service.exception.ResourceNotFoundException;
import com.vetcare.user_auth_service.repo.UserRepository;
import com.vetcare.user_auth_service.shared.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmployeeClient employeeClient;
    private final RabbitTemplate rabbitTemplate;
    
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
    
        // Rabbite MQ
        try {
            // RabbitMQ ekata yawanna Log object eka hadanawa
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(savedUser.getId())
                    .action("USER_REGISTERED")
                    .serviceName("user-auth-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
        
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("Failed to send log to RabbitMQ: " + e.getMessage());
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
    
    @Override
    @Transactional
    public String updateUser(Long userId, UserRegisterDto userDto, EmployeeDto employeeDto, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    
       
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
            user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(user);
    
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String employeeJson = objectMapper.writeValueAsString(employeeDto);
        
        
            employeeClient.updateEmployee(userId, employeeJson, file);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating employee details: " + e.getMessage());
        }
    
        return "User and Employee Profile Updated Successfully!";
    }
    
    @Override
    @Transactional
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    
        try {
           
            employeeClient.deleteEmployee(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting employee files/data: " + e.getMessage());
        }
        
        userRepository.delete(user);
        
       //Rabbite MQ
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(userId)
                    .action("USER_DELETED")
                    .serviceName("user-auth-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
        
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("Failed to send log to RabbitMQ: " + e.getMessage());
        }
        
        
        return "User Profile and Associated Files Deleted Successfully!";
    }
    
    @Override
    public List<UserEmployeeResponseDto> getUsersByRole(String role) {
        // 1. Role eka anuwa DB eken users la gannawa
        List<User> users = userRepository.findByRole(role);
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
    
        // 2. E users lage IDs tika wenama List ekakata gannawa
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
    
        // 3. E IDs deela Feign eka haraha Employee data tika gannawa
        List<EmployeeDto> employees = employeeClient.getEmployeesByUserIds(userIds);
    
        // 4. Data match karala, Image URL eka hadala aluth DTO eka return karanawa
        return users.stream().map(user -> {
            // User ge id ekata galapena employee wa hoyagannawa
            EmployeeDto emp = employees.stream()
                    .filter(e -> e.getUserId().equals(user.getId()))
                    .findFirst()
                    .orElse(null);
        
            // API Gateway eka haraha image eka load wena Full URL eka hadanawa
            String imageUrl = null;
            if (emp != null && emp.getProfileImagePath() != null) {
                imageUrl = "http://localhost:8080/api/employees/image/" + emp.getProfileImagePath();
            }
        
            return new UserEmployeeResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    emp != null ? emp.getFirstName() : null,
                    emp != null ? emp.getLastName() : null,
                    emp != null ? emp.getContact() : null,
                    emp != null ? emp.getDesignation() : null,
                    imageUrl
            );
        }).collect(Collectors.toList());
    }
    
    
}
