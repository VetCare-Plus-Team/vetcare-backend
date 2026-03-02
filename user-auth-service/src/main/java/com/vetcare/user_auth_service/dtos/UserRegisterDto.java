package com.vetcare.user_auth_service.dtos;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;
    private String role;
}
