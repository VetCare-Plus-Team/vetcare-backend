package com.vetcare.pet_profile_service.service;

import com.vetcare.pet_profile_service.dto.OwnerDto;
import com.vetcare.pet_profile_service.dto.PetDto;
import com.vetcare.pet_profile_service.dto.PetResponseDto;

public interface PetService {

    OwnerDto registerOwner(OwnerDto dto);

    OwnerDto getOwnerById(Long ownerId);

    OwnerDto updateOwner(Long ownerId, OwnerDto dto);

    void deleteOwner(Long ownerId);

    PetResponseDto registerPet(PetDto dto);

    void deletePet(Long petId);

    PetResponseDto updatePet(Long petId, PetDto dto);
}
