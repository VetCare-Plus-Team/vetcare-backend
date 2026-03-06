package com.vetcare.vaccination_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "calculation-service")
public interface CalculationClient {
    @GetMapping("/api/calculations/next-vaccine-date")
    LocalDate getNextDate(@RequestParam("vaccineName") String vaccineName, @RequestParam("administeredDate") LocalDate administeredDate);
}
