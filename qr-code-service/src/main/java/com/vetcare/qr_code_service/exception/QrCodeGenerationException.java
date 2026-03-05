package com.vetcare.qr_code_service.exception;

public class QrCodeGenerationException extends RuntimeException {

    public QrCodeGenerationException(String message) {
        super(message);
    }

    public QrCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
