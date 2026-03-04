package com.vetcare.qr_code_service.controller;

import com.vetcare.qr_code_service.service.QrGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {
    private final QrGeneratorService qrGeneratorService;
    
    @Value("${qr.code.path}")
    private String qrCodePath;
    
    @PostMapping("/generate/{petId}")
    public ResponseEntity<String> generateQrForPet(@PathVariable Long petId) {
        try {
            String fileName = qrGeneratorService.generateQrCode(petId);
            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating QR");
        }
    }
    
    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteQrCode(@PathVariable String fileName) {
        qrGeneratorService.deleteQrCodeFile(fileName);
        return ResponseEntity.ok("QR Code deleted successfully");
    }
    
    // Qr code serve as png
    @GetMapping("/image/{fileName}")
    public void serveQrImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        File file = new File(qrCodePath + File.separator + fileName);
        
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        InputStream resourceFile = new FileInputStream(file);
        response.setContentType(MediaType.IMAGE_PNG_VALUE); // PNG format eken yawanawa
        StreamUtils.copy(resourceFile, response.getOutputStream());
    }
    
    
}
