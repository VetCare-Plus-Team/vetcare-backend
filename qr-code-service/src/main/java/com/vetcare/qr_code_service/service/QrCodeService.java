package com.vetcare.qr_code_service.service;

import com.vetcare.qr_code_service.dto.QrCodeResponse;

import java.util.List;

public interface QrCodeService {

    /**
     * Generates a QR code for a given appointment and saves it to the database.
     * Fetches appointment details from appointment-service via Feign.
     *
     * @param appointmentId the ID of the appointment
     * @return QrCodeResponse containing the generated QR data
     */
    QrCodeResponse generateAndSaveQrCode(Long appointmentId);

    /**
     * Retrieves a QR code by appointment ID.
     *
     * @param appointmentId the ID of the appointment
     * @return QrCodeResponse for the matching record
     */
    QrCodeResponse getQrCodeByAppointmentId(Long appointmentId);

    /**
     * Retrieves all QR codes associated with a specific pet.
     *
     * @param petId the ID of the pet
     * @return list of QrCodeResponse for all appointments of this pet
     */
    List<QrCodeResponse> getQrCodesByPetId(Long petId);

    /**
     * Returns the raw PNG image bytes of a QR code for a given appointment.
     *
     * @param appointmentId the ID of the appointment
     * @return byte array of the PNG image
     */
    byte[] getQrCodeImageByAppointmentId(Long appointmentId);
}
