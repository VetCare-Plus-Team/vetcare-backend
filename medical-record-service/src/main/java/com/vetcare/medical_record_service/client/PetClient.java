package com.vetcare.medical_record_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pet-profile-service")
public interface PetClient {
    @GetMapping("/api/pets/pet/{petId}")
    Object getPetDetails(@PathVariable("petId") Long petId);
}
