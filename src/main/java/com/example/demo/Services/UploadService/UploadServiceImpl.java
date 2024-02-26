package com.example.demo.Services.UploadService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.UploadDto;
import com.example.demo.Models.Upload;
import com.example.demo.Repositories.UploadRepository;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService{

    private final UploadRepository uploadRepository;

    public UploadServiceImpl(UploadRepository uploadRepository){
        this.uploadRepository = uploadRepository;
    }

    public Iterable<Upload> getAllUploads(){
        return uploadRepository.findAll();
    }

    public Upload saveUpload(UploadDto uploadDto){
        Upload upload = new Upload();
        upload.setPath(uploadDto.getPath());
        return uploadRepository.save(upload);
    }
}
