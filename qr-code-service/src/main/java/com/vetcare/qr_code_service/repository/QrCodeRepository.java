package com.vetcare.qr_code_service.repository;

import com.vetcare.qr_code_service.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    Optional<QrCode> findByAppointmentId(Long appointmentId);

    List<QrCode> findAllByPetId(Long petId);

    boolean existsByAppointmentId(Long appointmentId);
}
