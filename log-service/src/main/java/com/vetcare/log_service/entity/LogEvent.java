package com.vetcare.log_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    private String action; // e.g., "USER_REGISTERED", "USER_DELETED"
    private String serviceName;
    private String ipAddress;
    private LocalDateTime timestamp;
}
