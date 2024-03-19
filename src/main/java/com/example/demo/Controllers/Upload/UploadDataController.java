package com.example.demo.Controllers.Upload;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.UploadData;
import com.example.demo.Services.UploadService.UploadDataServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/uploads")
@AllArgsConstructor
public class UploadDataController {

    private final UploadDataServiceImpl uploadDataServiceImpl;


    @PostMapping("/fileSystem/image")
    public ResponseEntity<Object> uploadDataToFIleSystem(@RequestParam("upload") MultipartFile file, @RequestParam("brandId") Long brandId) {
        try {
            return uploadDataServiceImpl.uploadDataToFileSystem(file, brandId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save data: " + e.getMessage());
        }
    }



    @SuppressWarnings("null")
    @GetMapping("/fileSystem/{uploadId}/{brandId}")
    public ResponseEntity<?> viewImageFromFileSystem(@PathVariable Long uploadId, @PathVariable Long brandId) {
        try {
            byte[] imageData = uploadDataServiceImpl.viewUploadDataFromFileSystem(uploadId, brandId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found for uploadId: " + uploadId);
        }
    }

    @GetMapping("/uploads/pdf")
    public ResponseEntity<?> getPdfUploads() {
        try {
            Iterable<UploadData> pdfUploads = uploadDataServiceImpl.getPdfUploads();
            return ResponseEntity.ok(pdfUploads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve PDF uploads: " + e.getMessage());
        }
    }


    @GetMapping("/uploads/images")
    public ResponseEntity<?> getImageUploads() {
        try {
            Iterable<UploadData> imageUploads = uploadDataServiceImpl.getImageUploads();
            return ResponseEntity.ok(imageUploads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve image uploads: " + e.getMessage());
        }
    }

    @GetMapping("/uploads/pdf/{brandId}")
    public ResponseEntity<?> getPdfUploadsByBrandId(@PathVariable Long brandId) {
        try {
            Iterable<UploadData> pdfUploads = uploadDataServiceImpl.getPdfUploadsByBrandId(brandId);
            return ResponseEntity.ok(pdfUploads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve PDF uploads: " + e.getMessage());
        }
    }

    @GetMapping("/uploads/images/{brandId}")
    public ResponseEntity<?> getImageUploadsByBrandId(@PathVariable Long brandId) {
        try {
            Iterable<UploadData> imageUploads = uploadDataServiceImpl.getImageUploadsByBrandId(brandId);
            return ResponseEntity.ok(imageUploads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve image uploads: " + e.getMessage());
        }
    }
    @GetMapping("/uploads/all")
    public ResponseEntity<?> getAllUploadedData() {
        try {
            Iterable<UploadData> allUploadedData = uploadDataServiceImpl.getAllUploadedData();
            return ResponseEntity.ok(allUploadedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve all uploads: " + e.getMessage());
        }
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

//    @GetMapping("/fileSystem/{uploadId}")
//    public ResponseEntity<?> viewImageFromFileSystem(@PathVariable Long uploadId) {
//        try {
//            byte[] imageData = uploadDataServiceImpl.viewUploadDataFromFileSystem(uploadId);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_PNG)
//                    .body(imageData);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Image not found for uploadId: " + uploadId);
//        }
//    }
//    @PostMapping("/fileSystem/image")
//    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("upload")MultipartFile file) throws IOException {
//        String uploadImage = uploadDataServiceImpl.uploadImageDataToFileSystem(file);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }