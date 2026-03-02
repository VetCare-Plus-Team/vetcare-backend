package com.vetcare.employee_service.file;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService{
    
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = path + File.separator + fileName;
    
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
    
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
    
    @Override
    public InputStream getResourcesFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
    
    @Override
    public void deleteFile(String path, String fileName) throws IOException {
        if (fileName != null && !fileName.isEmpty()) {
            Path filePath = Paths.get(path + File.separator + fileName);
            Files.deleteIfExists(filePath);
        }
    }
    
    
}
