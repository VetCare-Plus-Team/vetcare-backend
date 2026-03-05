package com.vetcare.log_service.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogMessageDto {
    private Long userId;
    private String action;
    private String serviceName;
    private String ipAddress;
    private LocalDateTime timestamp;
}
