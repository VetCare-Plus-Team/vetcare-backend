package com.vetcare.qr_code_service.client;

import com.vetcare.qr_code_service.dto.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "appointment-service")
public interface AppointmentClient {

    @GetMapping("/api/appointments/{appointmentId}")
    AppointmentResponse getAppointmentById(@PathVariable("appointmentId") Long appointmentId);
}
