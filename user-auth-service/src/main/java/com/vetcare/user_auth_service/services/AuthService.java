package com.vetcare.user_auth_service.services;

import com.vetcare.user_auth_service.dtos.AuthRequestDto;
import com.vetcare.user_auth_service.dtos.AuthResponseDto;
import com.vetcare.user_auth_service.dtos.EmployeeDto;
import com.vetcare.user_auth_service.dtos.UserRegisterDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    String registerFullUser(UserRegisterDto userDto, EmployeeDto employeeDto, MultipartFile file);
    AuthResponseDto login(AuthRequestDto request);
}
