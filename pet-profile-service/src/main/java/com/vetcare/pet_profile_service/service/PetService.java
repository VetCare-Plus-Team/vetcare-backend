package com.vetcare.pet_profile_service.service;

import com.vetcare.pet_profile_service.dtos.PetDto;
import com.vetcare.pet_profile_service.entity.Pet;
import com.vetcare.pet_profile_service.repo.PetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

  private final PetRepository petRepository;
  private final ModelMapper modelMapper;

  public PetDto createPet(PetDto petDto) {
    Pet pet = modelMapper.map(petDto, Pet.class);
    Pet saved = petRepository.save(pet);
    return modelMapper.map(saved, PetDto.class);
  }

  public List<PetDto> getAllPets() {
    return petRepository.findAll().stream()
        .map(pet -> modelMapper.map(pet, PetDto.class))
        .collect(Collectors.toList());
  }

  public List<PetDto> getPetsByOwner(Long ownerId) {
    return petRepository.findByOwnerId(ownerId).stream()
        .map(pet -> modelMapper.map(pet, PetDto.class))
        .collect(Collectors.toList());
  }

  public PetDto getPetById(Long id) {
    Pet pet = petRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
    return modelMapper.map(pet, PetDto.class);
  }

  public PetDto updatePet(Long id, PetDto petDto) {
    Pet existingPet = petRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));

    modelMapper.map(petDto, existingPet);
    existingPet.setId(id); // Ensure ID is fixed
    Pet updated = petRepository.save(existingPet);
    return modelMapper.map(updated, PetDto.class);
  }

  public void deletePet(Long id) {
    petRepository.deleteById(id);
  }
}
