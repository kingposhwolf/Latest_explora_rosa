package com.example.demo.Services.TitleService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.TitleDto;
import com.example.demo.Models.Title;
import com.example.demo.Repositories.TitleRepository;

@Service
public class TitleServiceImpl implements TitleService{

    private static final Logger logger = LoggerFactory.getLogger(TitleServiceImpl.class);

    private final TitleRepository titleRepository;

    public TitleServiceImpl(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public ResponseEntity<Object> getAllTitle() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(titleRepository.findAll());
        } catch (Exception exception) {
            logger.error("User saving failed" + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
    }

    @Override
    public ResponseEntity<Object> saveTitle(TitleDto titleDto) {
        try{
        Optional<Title> existingTitle = titleRepository.findByName(titleDto.getName());

        if (existingTitle.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title Already Exist.");
        }

        Title title = new Title();
        title.setName(titleDto.getName());
        titleRepository.save(title);

        return ResponseEntity.status(201).body("message: "+"User saved successfully");
        
        }catch(Exception exception){
            logger.error("User saving failed" + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
    }
    
}
