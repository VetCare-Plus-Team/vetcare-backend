package com.vetcare.qr_code_service;

import com.vetcare.qr_code_service.client.AppointmentClient;
import com.vetcare.qr_code_service.dto.AppointmentResponse;
import com.vetcare.qr_code_service.dto.QrCodeResponse;
import com.vetcare.qr_code_service.exception.QrCodeNotFoundException;
import com.vetcare.qr_code_service.model.QrCode;
import com.vetcare.qr_code_service.repository.QrCodeRepository;
import com.vetcare.qr_code_service.serviceImpl.QrCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private AppointmentClient appointmentClient;

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    private AppointmentResponse mockAppointment;
    private QrCode mockQrCode;

    @BeforeEach
    void setUp() {
        mockAppointment = AppointmentResponse.builder()
                .appointmentId(1L)
                .petId(10L)
                .petName("Buddy")
                .ownerName("John Doe")
                .veterinarianName("Dr. Smith")
                .appointmentDate(LocalDate.of(2026, 4, 15))
                .appointmentTime(LocalTime.of(10, 30))
                .clinicName("VetCare Clinic")
                .clinicAddress("123 Main St")
                .reason("Annual checkup")
                .status("CONFIRMED")
                .build();

        mockQrCode = QrCode.builder()
                .id(1L)
                .appointmentId(1L)
                .petId(10L)
                .qrImageBase64(Base64.getEncoder().encodeToString("fake-image".getBytes()))
                .qrContent("VetCare Appointment details...")
                .active(true)
                .build();
    }

    @Test
    void generateAndSaveQrCode_ShouldGenerateAndReturnResponse_WhenNotExists() {
        when(qrCodeRepository.existsByAppointmentId(1L)).thenReturn(false);
        when(appointmentClient.getAppointmentById(1L)).thenReturn(mockAppointment);
        when(qrCodeRepository.save(any(QrCode.class))).thenReturn(mockQrCode);

        QrCodeResponse response = qrCodeService.generateAndSaveQrCode(1L);

        assertThat(response).isNotNull();
        assertThat(response.getAppointmentId()).isEqualTo(1L);
        assertThat(response.getPetId()).isEqualTo(10L);
        verify(qrCodeRepository, times(1)).save(any(QrCode.class));
    }

    @Test
    void generateAndSaveQrCode_ShouldReturnExisting_WhenAlreadyExists() {
        when(qrCodeRepository.existsByAppointmentId(1L)).thenReturn(true);
        when(qrCodeRepository.findByAppointmentId(1L)).thenReturn(Optional.of(mockQrCode));

        QrCodeResponse response = qrCodeService.generateAndSaveQrCode(1L);

        assertThat(response).isNotNull();
        assertThat(response.getAppointmentId()).isEqualTo(1L);
        verify(appointmentClient, never()).getAppointmentById(anyLong());
        verify(qrCodeRepository, never()).save(any());
    }

    @Test
    void getQrCodeByAppointmentId_ShouldReturnResponse_WhenFound() {
        when(qrCodeRepository.findByAppointmentId(1L)).thenReturn(Optional.of(mockQrCode));

        QrCodeResponse response = qrCodeService.getQrCodeByAppointmentId(1L);

        assertThat(response).isNotNull();
        assertThat(response.getAppointmentId()).isEqualTo(1L);
    }

    @Test
    void getQrCodeByAppointmentId_ShouldThrow_WhenNotFound() {
        when(qrCodeRepository.findByAppointmentId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> qrCodeService.getQrCodeByAppointmentId(99L))
                .isInstanceOf(QrCodeNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getQrCodesByPetId_ShouldReturnList_WhenFound() {
        when(qrCodeRepository.findAllByPetId(10L)).thenReturn(List.of(mockQrCode));

        List<QrCodeResponse> responses = qrCodeService.getQrCodesByPetId(10L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getPetId()).isEqualTo(10L);
    }

    @Test
    void getQrCodesByPetId_ShouldThrow_WhenNoneFound() {
        when(qrCodeRepository.findAllByPetId(99L)).thenReturn(List.of());

        assertThatThrownBy(() -> qrCodeService.getQrCodesByPetId(99L))
                .isInstanceOf(QrCodeNotFoundException.class);
    }
}
