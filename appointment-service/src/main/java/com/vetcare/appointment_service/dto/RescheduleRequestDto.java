package com.vetcare.appointment_service.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RescheduleRequestDto {
    private LocalDate newDate;
    private LocalTime newTime;
}
