package com.vetcare.user_auth_service.dtos;


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
