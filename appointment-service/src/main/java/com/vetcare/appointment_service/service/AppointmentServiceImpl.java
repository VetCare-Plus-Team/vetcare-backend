package com.vetcare.appointment_service.service;

import com.vetcare.appointment_service.config.RabbitMQConfig;
import com.vetcare.appointment_service.dto.AppointmentRequestDto;
import com.vetcare.appointment_service.dto.LogMessageDto;
import com.vetcare.appointment_service.dto.RescheduleRequestDto;
import com.vetcare.appointment_service.entity.Appointment;
import com.vetcare.appointment_service.enums.AppointmentStatus;
import com.vetcare.appointment_service.exception.ResourceNotFoundException;
import com.vetcare.appointment_service.repo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl {

    private final AppointmentRepository appointmentRepository;
    private final RabbitTemplate rabbitTemplate;

    // 1. Book Appointment
    public Appointment bookAppointment(AppointmentRequestDto dto) {
        Appointment appointment = new Appointment();
        appointment.setPetId(dto.getPetId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Default status

        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendLog("APPOINTMENT_BOOKED", savedAppointment.getId());
        return savedAppointment;
    }

    // 2. Full Update Appointment
    public Appointment updateAppointment(Long id, AppointmentRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointment.setPetId(dto.getPetId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());

        Appointment updated = appointmentRepository.save(appointment);
        sendLog("APPOINTMENT_UPDATED", updated.getId());
        return updated;
    }

    // 3. Reschedule Appointment
    public Appointment rescheduleAppointment(Long id, RescheduleRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointment.setAppointmentDate(dto.getNewDate());
        appointment.setAppointmentTime(dto.getNewTime());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment updated = appointmentRepository.save(appointment);
        sendLog("APPOINTMENT_RESCHEDULED", updated.getId());
        return updated;
    }

    // 3. Update Status (e.g., to COMPLETED or CANCELLED)
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        sendLog("APPOINTMENT_STATUS_UPDATED_TO_" + status.name(), updated.getId());
        return updated;
    }

    // 4. Delete Appointment
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointmentRepository.delete(appointment);
        sendLog("APPOINTMENT_DELETED", id);
    }

    // 5. Get All (Calendar View Base)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // 6. Get by Doctor (Doctor's Schedule)
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // 7. Get by Date (Daily View)
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    // RabbitMQ Log Sender
    private void sendLog(String action, Long appointmentId) {
        try {
            LogMessageDto logDto = LogMessageDto.builder()
                    .userId(appointmentId) // Methana Appointment ID eka yawanawa reference ekata
                    .action(action)
                    .serviceName("appointment-service")
                    .ipAddress("N/A")
                    .timestamp(LocalDateTime.now())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, logDto);
        } catch (Exception e) {
            System.out.println("Log failed: " + e.getMessage());
        }
    }

}
