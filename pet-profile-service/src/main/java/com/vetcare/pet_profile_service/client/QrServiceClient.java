package com.vetcare.pet_profile_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "qr-code-service")
public interface QrServiceClient {
    @PostMapping("/api/qr/generate/{petId}")
    String generateQrForPet(@PathVariable("petId") Long petId);
    
    
    @DeleteMapping("/api/qr/{fileName}")
    void deleteQrCode(@PathVariable("fileName") String fileName);
}
