package com.vetcare.log_service.service;

import com.vetcare.log_service.dto.LogResponseDto;

import java.util.List;

public interface LogService {
    List<LogResponseDto> getAllLogs();
}
