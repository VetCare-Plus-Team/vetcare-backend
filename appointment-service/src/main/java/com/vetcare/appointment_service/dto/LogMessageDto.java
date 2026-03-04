package com.vetcare.appointment_service.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogMessageDto {
    private Long userId;
    private String action;
    private String serviceName;
    private String ipAddress;
    private LocalDateTime timestamp;
}
