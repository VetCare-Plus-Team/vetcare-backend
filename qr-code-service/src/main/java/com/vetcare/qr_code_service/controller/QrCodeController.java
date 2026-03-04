package com.vetcare.qr_code_service.controller;

import com.vetcare.qr_code_service.dto.QrCodeResponse;
import com.vetcare.qr_code_service.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qr-codes")
@RequiredArgsConstructor
@Slf4j
public class QrCodeController {

    private final QrCodeService qrCodeService;

    /**
     * POST /api/qr-codes/generate/{appointmentId}
     * Generates and saves a QR code for an appointment.
     * Returns Base64 encoded QR data.
     */
    @PostMapping("/generate/{appointmentId}")
    public ResponseEntity<QrCodeResponse> generateQrCode(@PathVariable Long appointmentId) {
        log.info("POST /api/qr-codes/generate/{}", appointmentId);
        QrCodeResponse response = qrCodeService.generateAndSaveQrCode(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/qr-codes/appointment/{appointmentId}
     * Retrieves QR code data (Base64) by appointment ID.
     */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<QrCodeResponse> getQrCodeByAppointmentId(@PathVariable Long appointmentId) {
        log.info("GET /api/qr-codes/appointment/{}", appointmentId);
        QrCodeResponse response = qrCodeService.getQrCodeByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/qr-codes/pet/{petId}
     * Retrieves all QR codes associated with a pet.
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<QrCodeResponse>> getQrCodesByPetId(@PathVariable Long petId) {
        log.info("GET /api/qr-codes/pet/{}", petId);
        List<QrCodeResponse> responses = qrCodeService.getQrCodesByPetId(petId);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/qr-codes/appointment/{appointmentId}/image
     * Returns the QR code as a raw PNG image (binary).
     */
    @GetMapping(value = "/appointment/{appointmentId}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCodeImage(@PathVariable Long appointmentId) {
        log.info("GET /api/qr-codes/appointment/{}/image", appointmentId);
        byte[] imageBytes = qrCodeService.getQrCodeImageByAppointmentId(appointmentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
