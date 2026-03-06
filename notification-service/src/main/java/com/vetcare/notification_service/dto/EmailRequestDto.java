package com.vetcare.notification_service.dto;


import lombok.Data;

@Data
public class EmailRequestDto {
    private String to;
    private String subject;
    private String message;
}
