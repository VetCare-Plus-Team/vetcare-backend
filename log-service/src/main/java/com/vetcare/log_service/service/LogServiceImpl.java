package com.vetcare.log_service.service;

import com.vetcare.log_service.dto.LogResponseDto;
import com.vetcare.log_service.entity.LogEvent;
import com.vetcare.log_service.repo.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LogServiceImpl implements  LogService{
    private final LogRepository logRepository;
    @Override
    public List<LogResponseDto> getAllLogs() {
        List<LogEvent> logs = logRepository.findAll();
    
        return logs.stream().map(log -> LogResponseDto.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .action(log.getAction())
                .serviceName(log.getServiceName())
                .ipAddress(log.getIpAddress())
                .timestamp(log.getTimestamp())
                .build()
        ).collect(Collectors.toList());
    }
}
