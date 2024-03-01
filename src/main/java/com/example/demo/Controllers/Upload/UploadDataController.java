package com.example.demo.Controllers.Upload;

import com.example.demo.Services.UploadService.UploadDataServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/uploads")
public class UploadDataController {

    private final UploadDataServiceImpl uploadDataServiceImpl;

    public UploadDataController(UploadDataServiceImpl uploadDataServiceImpl){
        this.uploadDataServiceImpl = uploadDataServiceImpl;
    }
//    @PostMapping("/fileSystem/image")
//    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("upload")MultipartFile file) throws IOException {
//        String uploadImage = uploadDataServiceImpl.uploadImageDataToFileSystem(file);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }
    @PostMapping("/fileSystem/image")
    public ResponseEntity<Object> uploadImageToFIleSystem(@RequestParam("upload") MultipartFile file) {
        try {
            return uploadDataServiceImpl.uploadDataToFileSystem(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save data: " + e.getMessage());
        }
    }

//    @GetMapping("/fileSystem/{uploadId}")
//    public ResponseEntity<?> viewImageFromFileSystem(@PathVariable Long uploadId) throws IOException {
//        byte[] imageData= uploadDataServiceImpl.viewImageDataFromFileSystem(uploadId);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageData);
//
//    }

    @GetMapping("/fileSystem/{uploadId}")
    public ResponseEntity<?> viewImageFromFileSystem(@PathVariable Long uploadId) {
        try {
            byte[] imageData = uploadDataServiceImpl.viewUploadDataFromFileSystem(uploadId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found for uploadId: " + uploadId);
        }
    }

}
