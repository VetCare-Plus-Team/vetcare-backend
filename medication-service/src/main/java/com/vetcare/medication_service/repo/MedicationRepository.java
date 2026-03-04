package com.vetcare.medication_service.repo;

import com.vetcare.medication_service.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication,Long>
{
    List<Medication> findByPetId(Long petId);
}
