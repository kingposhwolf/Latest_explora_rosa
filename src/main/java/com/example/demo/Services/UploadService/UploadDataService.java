package com.example.demo.Services.UploadService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadDataService {

    ResponseEntity<Object> uploadDataToFileSystem(MultipartFile upload) throws IOException;
    byte[] viewUploadDataFromFileSystem(Long uploadId) throws IOException;
}
