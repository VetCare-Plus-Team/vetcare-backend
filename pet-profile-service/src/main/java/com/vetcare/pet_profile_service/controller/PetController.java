package com.vetcare.pet_profile_service.controller;

import com.vetcare.pet_profile_service.dto.OwnerDto;
import com.vetcare.pet_profile_service.dto.PetDto;
import com.vetcare.pet_profile_service.service.PetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetServiceImpl petService;

    @PostMapping("/owner")
    public ResponseEntity<?> addOwner(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody OwnerDto dto) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can register Owners.");
        }
        return ResponseEntity.ok(petService.registerOwner(dto));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getOwnerById(
            @PathVariable("ownerId") Long ownerId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can view Owner details.");
        }
        return ResponseEntity.ok(petService.getOwnerById(ownerId));
    }

    @PutMapping("/owner/{ownerId}")
    public ResponseEntity<?> updateOwner(
            @PathVariable("ownerId") Long ownerId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody OwnerDto dto) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can update Owners.");
        }
        return ResponseEntity.ok(petService.updateOwner(ownerId, dto));
    }

    @DeleteMapping("/owner/{ownerId}")
    public ResponseEntity<?> deleteOwner(
            @PathVariable("ownerId") Long ownerId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can delete Owners.");
        }
        petService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner deleted successfully!");
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody PetDto dto) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can register Pets.");
        }
        return ResponseEntity.ok(petService.registerPet(dto));
    }

    @PutMapping("/pet/{petId}")
    public ResponseEntity<?> updatePet(
            @PathVariable("petId") Long petId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody PetDto dto) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can update Pets.");
        }
        return ResponseEntity.ok(petService.updatePet(petId, dto));
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<?> deletePet(
            @PathVariable("petId") Long petId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can delete Pets.");
        }
        petService.deletePet(petId);
        return ResponseEntity.ok("Pet and associated QR Code deleted successfully!");
    }

    // Helper method to check role access
    private boolean isAdmin(String role) {
        return role != null && (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("STAFF")
                || role.equalsIgnoreCase("DOCTOR"));
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getAllOwners(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can view Owners.");
        }
        return ResponseEntity.ok(petService.getAllOwners());
    }

    @GetMapping("/pet")
    public ResponseEntity<?> getAllPets(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can view Pets.");
        }
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getPetById(
            @PathVariable("petId") Long petId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can view Pet details.");
        }
        return ResponseEntity.ok(petService.getPetById(petId));
    }
}
