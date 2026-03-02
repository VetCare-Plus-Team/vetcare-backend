package com.vetcare.user_auth_service.services;

import com.vetcare.user_auth_service.dtos.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuthService {
    String registerFullUser(UserRegisterDto userDto, EmployeeDto employeeDto, MultipartFile file);
    AuthResponseDto login(AuthRequestDto request);
    String updateUser(Long userId, UserRegisterDto userDto, EmployeeDto employeeDto, MultipartFile file);
    String deleteUser(Long userId);
    List<UserEmployeeResponseDto> getUsersByRole(String role);
}
