package com.vetcare.log_service.service;


import com.vetcare.log_service.config.RabbitMQConfig;
import com.vetcare.log_service.dto.LogMessageDto;
import com.vetcare.log_service.entity.LogEvent;
import com.vetcare.log_service.repo.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogEventListener {
    private final LogRepository logRepository;
    
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeLogMessage(LogMessageDto message) {
        System.out.println("Log Event Received: " + message.getAction() + " for User ID: " + message.getUserId());
        
        LogEvent logEvent = LogEvent.builder()
                .userId(message.getUserId())
                .action(message.getAction())
                .serviceName(message.getServiceName())
                .ipAddress(message.getIpAddress())
                .timestamp(message.getTimestamp())
                .build();
        
        logRepository.save(logEvent);
    }
}
