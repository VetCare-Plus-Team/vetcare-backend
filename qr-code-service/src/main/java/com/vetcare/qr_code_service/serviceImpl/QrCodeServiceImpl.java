package com.vetcare.qr_code_service.serviceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vetcare.qr_code_service.client.AppointmentClient;
import com.vetcare.qr_code_service.dto.AppointmentResponse;
import com.vetcare.qr_code_service.dto.QrCodeResponse;
import com.vetcare.qr_code_service.exception.QrCodeGenerationException;
import com.vetcare.qr_code_service.exception.QrCodeNotFoundException;
import com.vetcare.qr_code_service.model.QrCode;
import com.vetcare.qr_code_service.repository.QrCodeRepository;
import com.vetcare.qr_code_service.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final AppointmentClient appointmentClient;

    @Value("${qr.code.width:300}")
    private int qrWidth;

    @Value("${qr.code.height:300}")
    private int qrHeight;

    @Override
    public QrCodeResponse generateAndSaveQrCode(Long appointmentId) {
        log.info("Generating QR code for appointmentId: {}", appointmentId);

        // If QR already exists, return existing one
        if (qrCodeRepository.existsByAppointmentId(appointmentId)) {
            log.info("QR code already exists for appointmentId: {}. Returning existing.", appointmentId);
            return getQrCodeByAppointmentId(appointmentId);
        }

        // Fetch appointment details from appointment-service
        AppointmentResponse appointment = appointmentClient.getAppointmentById(appointmentId);

        // Build the QR code content string from appointment details
        String qrContent = buildQrContent(appointment);

        // Generate QR code image as byte array
        byte[] qrImageBytes = generateQrCodeImage(qrContent);

        // Encode image to Base64 string for storage
        String base64Image = Base64.getEncoder().encodeToString(qrImageBytes);

        // Persist the QR code record
        QrCode qrCode = QrCode.builder()
                .appointmentId(appointmentId)
                .petId(appointment.getPetId())
                .qrImageBase64(base64Image)
                .qrContent(qrContent)
                .build();

        QrCode saved = qrCodeRepository.save(qrCode);
        log.info("QR code saved with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public QrCodeResponse getQrCodeByAppointmentId(Long appointmentId) {
        log.info("Fetching QR code for appointmentId: {}", appointmentId);

        QrCode qrCode = qrCodeRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new QrCodeNotFoundException(appointmentId, "appointment"));

        return mapToResponse(qrCode);
    }

    @Override
    public List<QrCodeResponse> getQrCodesByPetId(Long petId) {
        log.info("Fetching all QR codes for petId: {}", petId);

        List<QrCode> qrCodes = qrCodeRepository.findAllByPetId(petId);

        if (qrCodes.isEmpty()) {
            throw new QrCodeNotFoundException(petId, "pet");
        }

        return qrCodes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getQrCodeImageByAppointmentId(Long appointmentId) {
        log.info("Fetching QR code image bytes for appointmentId: {}", appointmentId);

        QrCode qrCode = qrCodeRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new QrCodeNotFoundException(appointmentId, "appointment"));

        return Base64.getDecoder().decode(qrCode.getQrImageBase64());
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    /**
     * Builds a human-readable string from appointment details to encode into the QR code.
     */
    private String buildQrContent(AppointmentResponse appointment) {
        return String.format(
                "VetCare Appointment\n" +
                "-------------------\n" +
                "Appointment ID : %d\n" +
                "Pet Name       : %s\n" +
                "Pet ID         : %d\n" +
                "Owner          : %s\n" +
                "Veterinarian   : %s\n" +
                "Date           : %s\n" +
                "Time           : %s\n" +
                "Clinic         : %s\n" +
                "Address        : %s\n" +
                "Reason         : %s\n" +
                "Status         : %s",
                appointment.getAppointmentId(),
                appointment.getPetName(),
                appointment.getPetId(),
                appointment.getOwnerName(),
                appointment.getVeterinarianName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getClinicName(),
                appointment.getClinicAddress(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }

    /**
     * Uses ZXing to generate a QR code PNG image from the given text content.
     */
    private byte[] generateQrCodeImage(String content) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code image: {}", e.getMessage());
            throw new QrCodeGenerationException("Failed to generate QR code image", e);
        }
    }

    /**
     * Maps a QrCode entity to a QrCodeResponse DTO.
     */
    private QrCodeResponse mapToResponse(QrCode qrCode) {
        return QrCodeResponse.builder()
                .id(qrCode.getId())
                .appointmentId(qrCode.getAppointmentId())
                .petId(qrCode.getPetId())
                .qrImageBase64(qrCode.getQrImageBase64())
                .qrContent(qrCode.getQrContent())
                .createdAt(qrCode.getCreatedAt())
                .active(qrCode.isActive())
                .build();
    }
}
