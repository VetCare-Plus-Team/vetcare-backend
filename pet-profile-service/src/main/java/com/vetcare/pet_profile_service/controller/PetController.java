package com.vetcare.pet_profile_service.controller;

import com.vetcare.pet_profile_service.dtos.PetDto;
import com.vetcare.pet_profile_service.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

  private final PetService petService;

  @PostMapping
  public ResponseEntity<PetDto> create(@RequestBody PetDto petDto) {
    return ResponseEntity.ok(petService.createPet(petDto));
  }

  @GetMapping
  public ResponseEntity<List<PetDto>> getAll() {
    return ResponseEntity.ok(petService.getAllPets());
  }

  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<List<PetDto>> getByOwner(@PathVariable Long ownerId) {
    return ResponseEntity.ok(petService.getPetsByOwner(ownerId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PetDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(petService.getPetById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PetDto> update(@PathVariable Long id, @RequestBody PetDto petDto) {
    return ResponseEntity.ok(petService.updatePet(id, petDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    petService.deletePet(id);
    return ResponseEntity.noContent().build();
  }
}
