package com.vetcare.notification_service.client;


import com.vetcare.notification_service.dto.PetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pet-profile-service")
public interface PetClient {
    @GetMapping("/api/pets/pet/{petId}")
    PetResponseDto getPetDetails(@PathVariable("petId") Long petId);
}
