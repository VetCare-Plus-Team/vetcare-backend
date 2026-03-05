package com.vetcare.pet_profile_service.service;

import com.vetcare.pet_profile_service.dto.OwnerDto;
import com.vetcare.pet_profile_service.dto.PetDto;
import com.vetcare.pet_profile_service.entity.Owner;
import com.vetcare.pet_profile_service.entity.Pet;

public interface PetService {
    
    Owner registerOwner(OwnerDto dto);
    
    Pet registerPet(PetDto dto);
    
    void deletePet(Long petId);
    
    Pet updatePet(Long petId, PetDto dto);
}
