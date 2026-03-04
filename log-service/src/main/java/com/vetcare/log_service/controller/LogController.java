package com.vetcare.log_service.controller;


import com.vetcare.log_service.dto.LogResponseDto;
import com.vetcare.log_service.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    
    @GetMapping
    public ResponseEntity<?> getAllLogs(@RequestHeader(value = "X-User-Role", required = false) String role) {
        
       
        if (role == null || !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN users can view system logs.");
        }
        
        
        List<LogResponseDto> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
