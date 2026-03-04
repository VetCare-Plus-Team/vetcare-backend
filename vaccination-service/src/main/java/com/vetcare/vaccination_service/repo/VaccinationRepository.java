package com.vetcare.vaccination_service.repo;


import com.vetcare.vaccination_service.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination,Long> {
    List<Vaccination> findByPetId(Long petId);
    
    List<Vaccination> findByNextDueDateBetween(LocalDate startDate, LocalDate endDate);
}
