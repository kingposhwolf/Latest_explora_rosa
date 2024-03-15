package com.example.demo.Services.UploadService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.Brand;
import com.example.demo.Models.UploadData;
import com.example.demo.Models.UserPost;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.UploadDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UploadDataServiceImpl implements UploadDataService {

    private static final Logger logger = LoggerFactory.getLogger(UploadDataServiceImpl.class);


    private final UploadDataRepository uploadDataRepository;

    private final BrandRepository brandRepository;

    public UploadDataServiceImpl(UploadDataRepository uploadDataRepository, BrandRepository brandRepository){
        this.uploadDataRepository = uploadDataRepository;
        this.brandRepository = brandRepository;
    }


    private final String folderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\uploadsData\\";

    @SuppressWarnings("null")
    @Transactional
    @Override
    public ResponseEntity<Object> uploadDataToFileSystem(MultipartFile upload, Long brandId) throws IOException {

        String uploadPath = folderPath + upload.getOriginalFilename();

        try {
            // Fetch the Brand entity using the provided brandId
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + brandId + " not found"));

            // Check if the content type is allowed
            String contentType = upload.getContentType();
            if (!isValidContentType(contentType)) {
                logger.error("Unsupported content type: {}", contentType);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
            }

            UploadData uploadData = uploadDataRepository.save(UploadData.builder()
                    .name(upload.getOriginalFilename())
                    .type(contentType)
                    .path(uploadPath)
                    .brand(brand) // Set the Brand entity
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

    private boolean isValidContentType(String contentType) {
        return contentType != null && (contentType.startsWith("image/") || contentType.equals("application/pdf"));
    }

    @Override
    public byte[] viewUploadDataFromFileSystem(Long uploadId, Long brandId) throws IOException {
        Optional<UploadData> uploadDataOptional = uploadDataRepository.findById(uploadId);
        if (uploadDataOptional.isPresent()) {
            UploadData uploadData = uploadDataOptional.get();
            // Check if the upload data belongs to the specified brandId
            if (uploadData.getBrand() == null || !uploadData.getBrand().getId().equals(brandId)) {
                logger.error("Verification Upload data with ID {} does not belong to brand with ID {}", uploadId, brandId);
                throw new IOException("Verification Upload data not found for brand ID: " + brandId);
            }
            String uploadPath = uploadData.getPath();
            return Files.readAllBytes(new File(uploadPath).toPath());
        } else {
            throw new IOException("Upload data not found for id: " + uploadId);
        }
    }

    @Override
    public Iterable<UploadData> getPdfUploads() {
        try {
            return uploadDataRepository.findByType("application/pdf");
        } catch (Exception e) {
            logger.error("Failed to retrieve PDF uploads: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve PDF uploads");
        }
    }

    @Override
    public Iterable<UploadData> getImageUploads() {
        try {
            return uploadDataRepository.findByTypeStartingWith("image/");
        } catch (Exception e) {
            logger.error("Failed to retrieve image uploads: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve image uploads");
        }
    }

    @Override
    public Iterable<UploadData> getPdfUploadsByBrandId(Long brandId) {
        try {
            return uploadDataRepository.findByBrandIdAndType(brandId, "application/pdf");
        } catch (Exception e) {
            logger.error("Failed to retrieve PDF uploads: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve PDF uploads");
        }
    }

    @Override
    public Iterable<UploadData> getImageUploadsByBrandId(Long brandId) {
        try {
            return uploadDataRepository.findByBrandIdAndTypeStartingWith(brandId, "image/");
        } catch (Exception e) {
            logger.error("Failed to retrieve image uploads: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve image uploads");
        }
    }

    @Override
    public Iterable<UploadData> getPDFAndImageUploadsByBrandId(Long brandId) {
        try {
            // Fetch uploads by brandId
            Iterable<UploadData> uploadsByBrandId = uploadDataRepository.findByBrandId(brandId);

            // Filter uploads to include only PDF and image files
            List<UploadData> pdfAndImageUploads = new ArrayList<>();
            for (UploadData upload : uploadsByBrandId) {
                String fileType = upload.getType();
                if (fileType != null && (fileType.startsWith("image/") || fileType.equals("application/pdf"))) {
                    pdfAndImageUploads.add(upload);
                }
            }

            return pdfAndImageUploads;
        } catch (Exception e) {
            logger.error("Failed to retrieve PDF and image uploads for brand ID {}: {}", brandId, e.getMessage());
            throw new RuntimeException("Failed to retrieve PDF and image uploads");
        }
    }


    @Override
    public Iterable<UploadData> getAllUploadedData() {
        try {
            return uploadDataRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve all verification uploads data: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve verification uploads");
        }
    }

//    @Override
//    public ResponseEntity<Object> checkPostContentType(Long postId) {
//        try {
//            // Fetch the post from the repository based on the postId
//            Optional<UserPost> optionalUserPost = userPostRepository.findById(postId);
//
//            if (optionalUserPost.isPresent()) {
//                UserPost userPost = optionalUserPost.get();
//                String postType = userPost.getType();
//
//                if (postType.equalsIgnoreCase("video")) {
//                    logger.info("Post with ID {} is a video.", postId);
//                    return ResponseEntity.ok("Video");
//                } else if (postType.equalsIgnoreCase("image")) {
//                    logger.info("Post with ID {} is an image.", postId);
//                    return ResponseEntity.ok("Image");
//                } else {
//                    logger.warn("Unknown post type for post with ID {}.", postId);
//                    return ResponseEntity.ok("Unknown");
//                }
//            } else {
//                logger.warn("Post with ID {} not found.", postId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
//            }
//        } catch (Exception e) {
//            logger.error("Failed to check post content type: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to check post content type: " + e.getMessage());
//        }
//    }
//}
}

