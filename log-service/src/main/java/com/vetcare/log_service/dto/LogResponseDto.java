package com.vetcare.log_service.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogResponseDto {
    private Long id;
    private Long userId;
    private String action;
    private String serviceName;
    private String ipAddress;
    private LocalDateTime timestamp;
}
