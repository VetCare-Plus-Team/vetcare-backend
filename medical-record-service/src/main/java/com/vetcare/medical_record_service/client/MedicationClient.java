package com.vetcare.medical_record_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "medication-service")
public interface MedicationClient {
    @GetMapping("/api/medications/pet/{petId}")
    List<Object> getMedications(@PathVariable("petId") Long petId);
}
