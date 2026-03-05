package com.vetcare.appointment_service.repo;

import com.vetcare.appointment_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  List<Appointment> findByPetId(Long petId);

  List<Appointment> findByDoctorId(Long doctorId);
}
