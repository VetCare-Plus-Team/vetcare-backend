package com.vetcare.medical_record_service.repo;

import com.vetcare.medical_record_service.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
  List<MedicalRecord> findByPetId(Long petId);
}
