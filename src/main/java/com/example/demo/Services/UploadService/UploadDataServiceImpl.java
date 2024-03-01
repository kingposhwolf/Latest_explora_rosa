package com.example.demo.Services.UploadService;

import com.example.demo.Dto.ResponseDetail;
import com.example.demo.Models.UploadData;
import com.example.demo.Repositories.UploadDataRepository;
import com.example.demo.Services.CityService.CityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class UploadDataServiceImpl implements UploadDataService {

    private static final Logger logger = LoggerFactory.getLogger(UploadDataServiceImpl.class);


    private final UploadDataRepository uploadDataRepository;

    public UploadDataServiceImpl(UploadDataRepository uploadDataRepository){
        this.uploadDataRepository = uploadDataRepository;
    }

    private final String folderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\uploadsData\\";


        @Transactional
        @Override
        public ResponseEntity<Object> uploadDataToFileSystem(MultipartFile upload) throws IOException {
            String uploadPath = folderPath + upload.getOriginalFilename();

            try {
                UploadData uploadData = uploadDataRepository.save(UploadData.builder()
                        .name(upload.getOriginalFilename())
                        .type(upload.getContentType())
                        .path(uploadPath)
                        .build());

                upload.transferTo(new File(uploadPath));

                if (uploadData != null) {
                    logger.info("File uploaded successfully: {}", uploadPath);
                    return ResponseEntity.status(201).body("File Uploaded Successfully! ");
                }
            } catch (Exception e) {
                logger.error("Failed to save data: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save data: " + e.getMessage());
            }
            logger.error("Failed to save data: Unknown error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save data: Unknown error");

    }

//    @Override
//    public byte[] viewImageDataFromFileSystem(Long uploadId) throws IOException {
//        Optional<UploadData> uploadDataOptional = uploadDataRepository.findById(uploadId);
//        String uploadPath=uploadDataOptional.get().getPath();
//        byte[] uploads = Files.readAllBytes(new File(uploadPath).toPath());
//        return uploads;
//    }

    @Override
    public byte[] viewUploadDataFromFileSystem(Long uploadId) throws IOException {
        Optional<UploadData> uploadDataOptional = uploadDataRepository.findById(uploadId);
        if (uploadDataOptional.isPresent()) {
            UploadData uploadData = uploadDataOptional.get();
            String uploadPath = uploadData.getPath();
            return Files.readAllBytes(new File(uploadPath).toPath());
        } else {
            throw new IOException("Upload data not found for id: " + uploadId);
        }
    }




}
