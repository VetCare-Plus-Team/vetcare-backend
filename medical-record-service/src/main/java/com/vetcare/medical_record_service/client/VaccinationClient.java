package com.vetcare.medical_record_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "vaccination-service")
public interface VaccinationClient {
    @GetMapping("/api/vaccinations/pet/{petId}")
    List<Object> getVaccinations(@PathVariable("petId") Long petId);
}
