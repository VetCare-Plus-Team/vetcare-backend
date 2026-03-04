package com.vetcare.notification_service.client;

import com.vetcare.notification_service.dto.VaccinationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "vaccination-service")
public interface VaccinationClient {
    @GetMapping("/api/vaccinations/upcoming")
    List<VaccinationDto> getUpcomingVaccines(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate);
}
