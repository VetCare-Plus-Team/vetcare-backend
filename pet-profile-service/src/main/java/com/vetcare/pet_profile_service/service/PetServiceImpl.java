package com.vetcare.pet_profile_service.service;

import com.vetcare.pet_profile_service.client.QrServiceClient;
import com.vetcare.pet_profile_service.config.RabbitMQConfig;
import com.vetcare.pet_profile_service.dto.LogMessageDto;
import com.vetcare.pet_profile_service.dto.OwnerDto;
import com.vetcare.pet_profile_service.dto.PetDto;
import com.vetcare.pet_profile_service.dto.PetResponseDto;
import com.vetcare.pet_profile_service.entity.Owner;
import com.vetcare.pet_profile_service.entity.Pet;
import com.vetcare.pet_profile_service.exception.ResourceNotFoundException;
import com.vetcare.pet_profile_service.repo.OwnerRepository;
import com.vetcare.pet_profile_service.repo.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{
    
    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final QrServiceClient qrServiceClient;
    private final RabbitTemplate rabbitTemplate;
    
    @Override
    public Owner registerOwner(OwnerDto dto) {
        Owner owner = new Owner();
        owner.setFirstName(dto.getFirstName());
        owner.setLastName(dto.getLastName());
        owner.setContact(dto.getContact());
        owner.setEmail(dto.getEmail());
        owner.setAddress(dto.getAddress());
        Owner savedOwner = ownerRepository.save(owner);
    
        sendLog("OWNER_REGISTERED", savedOwner.getId());
        return savedOwner;
    }
    
    @Override
    @Transactional
    public Pet registerPet(PetDto dto) {
        Owner owner = ownerRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found!"));
    
        // A. Register PET
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setAge(dto.getAge());
        pet.setGender(dto.getGender());
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);
    
        // B.Cretae QR to call qr service using feing client
        try {
            String qrFileName = qrServiceClient.generateQrForPet(savedPet.getId());
            savedPet.setQrCodeFileName(qrFileName);
            petRepository.save(savedPet);
        } catch (Exception e) {
            System.out.println("QR Code generation failed: " + e.getMessage());
            
        }
    
        // C. Create LOG
        sendLog("PET_REGISTERED", savedPet.getId());
    
        return savedPet;
    }
    
    
    
    @Transactional
    public Pet updatePet(Long petId, PetDto dto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
        
        Owner owner = ownerRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + dto.getOwnerId()));
        
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setAge(dto.getAge());
        pet.setGender(dto.getGender());
        pet.setOwner(owner); // Owner wa wenas karannath puluwan
        
        Pet updatedPet = petRepository.save(pet);
        
        sendLog("PET_UPDATED", updatedPet.getId());
        return updatedPet;
    }
    
    // 4. Pet wa Delete kireema (QR ekath ekkama)
    @Transactional
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
        
        // QR Code file eka QR service eken makala danawa
        if (pet.getQrCodeFileName() != null) {
            try {
                qrServiceClient.deleteQrCode(pet.getQrCodeFileName());
            } catch (Exception e) {
                System.out.println("Failed to delete QR code file: " + e.getMessage());
            }
        }
        
        petRepository.delete(pet);
        sendLog("PET_DELETED", petId);
    }
    
    private void sendLog(String action, Long targetId) {
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(targetId)
                    .action(action)
                    .serviceName("pet-profile-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("RabbitMQ Log Failed: " + e.getMessage());
        }
    }
    
    
    
    // 5. Get All Owners
    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(this::mapToOwnerDto)
                .collect(Collectors.toList());
    }
    
    // 6. Get All Pets
    public List<PetResponseDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::mapToPetResponseDto)
                .collect(Collectors.toList());
    }
    
    
    // 7. Get Pet By ID
    public PetResponseDto getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + petId));
        return mapToPetResponseDto(pet);
    }
    
    // --- Helper Methods ---
    private OwnerDto mapToOwnerDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setFirstName(owner.getFirstName());
        dto.setLastName(owner.getLastName());
        dto.setContact(owner.getContact());
        dto.setEmail(owner.getEmail());
        dto.setAddress(owner.getAddress());
        return dto;
    }
    
    private PetResponseDto mapToPetResponseDto(Pet pet) {
        PetResponseDto dto = new PetResponseDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setBreed(pet.getBreed());
        dto.setAge(pet.getAge());
        dto.setGender(pet.getGender());
        dto.setQrCodeFileName(pet.getQrCodeFileName());
        
        //  Generate QR Code Full URL
        if (pet.getQrCodeFileName() != null) {
            dto.setQrCodeUrl("http://localhost:8080/api/qr/image/" + pet.getQrCodeFileName());
        }
        
        if (pet.getOwner() != null) {
            dto.setOwner(mapToOwnerDto(pet.getOwner()));
        }
        return dto;
    }
    
    
    
    
}
