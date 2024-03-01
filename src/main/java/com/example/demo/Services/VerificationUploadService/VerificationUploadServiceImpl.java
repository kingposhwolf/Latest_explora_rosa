package com.example.demo.Services.VerificationUploadService;

import com.example.demo.Dto.VerificationUploadDto;
import com.example.demo.Models.Brand;
import com.example.demo.Models.UploadData;
import com.example.demo.Models.VerificationUpload;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.UploadDataRepository;
import com.example.demo.Repositories.VerificationUploadRepository;

public class VerificationUploadServiceImpl implements VerificationUploadService {

    private final VerificationUploadRepository verificationUploadRepository;

    private final BrandRepository brandRepository;

    private final UploadDataRepository uploadDataRepository;

    public VerificationUploadServiceImpl(VerificationUploadRepository verificationUploadRepository, BrandRepository brandRepository, UploadDataRepository uploadDataRepository){
        this.verificationUploadRepository = verificationUploadRepository;
        this.uploadDataRepository = uploadDataRepository;
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
        UploadData uploadData = uploadDataRepository.findById(verificationUploadDto.getUploadId()).orElseThrow(()-> new RuntimeException("UploadData not Found!"));
        verificationUpload.setBrand(brand);
        verificationUpload.setUploadData(uploadData);
        return verificationUploadRepository.save(verificationUpload);
    }
}
