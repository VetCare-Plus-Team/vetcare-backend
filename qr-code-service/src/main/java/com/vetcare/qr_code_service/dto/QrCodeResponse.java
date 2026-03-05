package com.vetcare.qr_code_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeResponse {

    private Long id;
    private Long appointmentId;
    private Long petId;
    private String qrImageBase64;
    private String qrContent;
    private LocalDateTime createdAt;
    private boolean active;
}
