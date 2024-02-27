package com.example.demo.Services.VerificationUploadService;

import com.example.demo.Dto.VerificationUploadDto;
import com.example.demo.Models.Brand;
import com.example.demo.Models.Upload;
import com.example.demo.Models.VerificationUpload;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.UploadRepository;
import com.example.demo.Repositories.VerificationUploadRepository;

public class VerificationUploadServiceImpl implements VerificationUploadService {

    private final VerificationUploadRepository verificationUploadRepository;

    private final BrandRepository brandRepository;

    private final UploadRepository uploadRepository;

    public VerificationUploadServiceImpl(VerificationUploadRepository verificationUploadRepository, BrandRepository brandRepository, UploadRepository uploadRepository){
        this.verificationUploadRepository = verificationUploadRepository;
        this.uploadRepository = uploadRepository;
        this.brandRepository = brandRepository;
    }

    public Iterable<VerificationUpload> getAllVerificationUploads(){
        return verificationUploadRepository.findAll();
    }

    @SuppressWarnings("null")
    public VerificationUpload saveVerificationUpload(VerificationUploadDto verificationUploadDto){
        //Instantiating all objects of the classes to be used
        VerificationUpload verificationUpload = new VerificationUpload();
        Brand brand = brandRepository.findById(verificationUploadDto.getBrandId()).orElseThrow(()->new RuntimeException("Brand Not Found!"));
        Upload upload = uploadRepository.findById(verificationUploadDto.getUploadId()).orElseThrow(()-> new RuntimeException("Upload not Found!"));
        verificationUpload.setBrand(brand);
        verificationUpload.setUpload(upload);
        return verificationUploadRepository.save(verificationUpload);
    }
}
