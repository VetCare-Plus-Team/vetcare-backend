package com.vetcare.user_auth_service.dtos;


import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;
}
