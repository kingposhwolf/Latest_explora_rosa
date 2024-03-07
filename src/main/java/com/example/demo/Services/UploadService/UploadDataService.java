package com.example.demo.Services.UploadService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.UploadData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadDataService {

    ResponseEntity<Object> uploadDataToFileSystem(MultipartFile upload, Long brandId) throws IOException;
    byte[] viewUploadDataFromFileSystem(Long uploadId, Long brandId) throws IOException;

    Iterable<UploadData> getPdfUploads();

    Iterable<UploadData> getImageUploads();

    Iterable<UploadData> getPdfUploadsByBrandId(Long brandId);

    Iterable<UploadData> getImageUploadsByBrandId(Long brandId);

    Iterable<UploadData> getPDFAndImageUploadsByBrandId(Long brandId);

    Iterable<UploadData> getAllUploadedData();
}
