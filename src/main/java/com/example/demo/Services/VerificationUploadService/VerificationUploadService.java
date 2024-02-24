package com.example.demo.Services.VerificationUploadService;

import com.example.demo.Dto.VerificationUploadDto;
import com.example.demo.Models.VerificationUpload;

public interface VerificationUploadService {

    Iterable<VerificationUpload> getAllVerificationUploads();

    VerificationUpload saveVerificationUpload(VerificationUploadDto verificationUploadDto);
}
