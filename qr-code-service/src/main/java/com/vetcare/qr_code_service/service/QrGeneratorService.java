package com.vetcare.qr_code_service.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QrGeneratorService {
    @Value("${qr.code.path}")
    private String qrCodePath;
    
    
    public String generateQrCode(Long petId) throws Exception {
        // URL WHEN SCAN QR (Frontend profile link )
//        String qrContent = "http://localhost:3000/pet-profile/" + petId;
        String qrContent = "http://localhost:5174/pet-profile/" + petId;
        
        String fileName = "PET_" + petId + "_" + System.currentTimeMillis() + ".png";
        String fullPath = qrCodePath + File.separator + fileName;
        
        // Create folder if do not exist
        File directory = new File(qrCodePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Create QR Code then save as PNG
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
        
        Path path = FileSystems.getDefault().getPath(fullPath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        
        return fileName; // Return the name for save PET Service
    }
    
    
    
    public void deleteQrCodeFile(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(qrCodePath + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    
    
}
