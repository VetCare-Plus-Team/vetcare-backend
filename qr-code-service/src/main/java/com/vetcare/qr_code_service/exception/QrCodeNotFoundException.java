package com.vetcare.qr_code_service.exception;

public class QrCodeNotFoundException extends RuntimeException {

    public QrCodeNotFoundException(String message) {
        super(message);
    }

    public QrCodeNotFoundException(Long id, String type) {
        super("QR code not found for " + type + " ID: " + id);
    }
}
