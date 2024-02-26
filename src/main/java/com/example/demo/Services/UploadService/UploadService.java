package com.example.demo.Services.UploadService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.UploadDto;
import com.example.demo.Models.Upload;

public interface UploadService {

    Iterable<Upload> getAllUploads();

    Upload saveUpload(UploadDto uploadDto);



}
