package com.vetcare.employee_service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadFile(String path, MultipartFile file) throws IOException;
    InputStream getResourcesFile(String path, String name) throws FileNotFoundException;
    void deleteFile(String path, String fileName) throws IOException;
    
}
