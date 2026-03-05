package com.vetcare.appointment_service.service;

import com.vetcare.appointment_service.dtos.AppointmentDto;
import com.vetcare.appointment_service.entity.Appointment;
import com.vetcare.appointment_service.repo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final ModelMapper modelMapper;

  public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
    Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
    appointment.setStatus("PENDING");
    Appointment saved = appointmentRepository.save(appointment);
    return modelMapper.map(saved, AppointmentDto.class);
  }

  public List<AppointmentDto> getAllAppointments() {
    return appointmentRepository.findAll().stream()
        .map(appointment -> modelMapper.map(appointment, AppointmentDto.class))
        .collect(Collectors.toList());
  }

  public AppointmentDto getAppointmentById(Long id) {
    Appointment appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    return modelMapper.map(appointment, AppointmentDto.class);
  }

  public void updateStatus(Long id, String status) {
    Appointment appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    appointment.setStatus(status);
    appointmentRepository.save(appointment);
  }
}
