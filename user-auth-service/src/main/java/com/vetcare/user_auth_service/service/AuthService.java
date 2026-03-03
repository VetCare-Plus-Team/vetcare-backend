package com.vetcare.user_auth_service.service;

import com.vetcare.user_auth_service.dtos.AuthRequestDto;
import com.vetcare.user_auth_service.dtos.AuthResponseDto;
import com.vetcare.user_auth_service.dtos.UserRegisterDto;
import com.vetcare.user_auth_service.entity.User;
import com.vetcare.user_auth_service.repo.UserRepository;
import com.vetcare.user_auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public void register(UserRegisterDto registerDto) {
    User user = new User();
    user.setUsername(registerDto.getUsername());
    user.setEmail(registerDto.getEmail());
    user.setPasswordHash(passwordEncoder.encode(registerDto.getPassword()));
    user.setRole(registerDto.getRole());
    user.setStatus("ACTIVE");
    userRepository.save(user);
  }

  public AuthResponseDto login(AuthRequestDto authRequest) {
    User user = userRepository.findByUsername(authRequest.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash())) {
      throw new RuntimeException("Invalid credentials");
    }

    String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId().toString());

    AuthResponseDto response = new AuthResponseDto();
    response.setToken(token);
    response.setUsername(user.getUsername());
    response.setRole(user.getRole());
    return response;
  }
}
