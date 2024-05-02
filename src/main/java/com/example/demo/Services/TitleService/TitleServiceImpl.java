package com.example.demo.Services.TitleService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.UserManagement.PersonalProfile.TitleDto;
import com.example.demo.Models.UserManagement.PersonalAccount.Title;
import com.example.demo.Repositories.TitleRepository;

import lombok.AllArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TitleServiceImpl implements TitleService{

    private static final Logger logger = LoggerFactory.getLogger(TitleServiceImpl.class);

    private final TitleRepository titleRepository;


    @Override
    public ResponseEntity<Object> getAllTitle() {
        try {
            Iterable<Title> titles = titleRepository.findAll();
            if(!titles.iterator().hasNext()){
                logger.error("\nThere is Request for Fetching All Titles, But Nothing Registered to the database ");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Titles registered in the Database");
            }else{
                logger.info("\nSuccessful fetched all Titles");
                return ResponseEntity.status(HttpStatus.OK).body(titles);
            }
        } catch (Exception exception) {
            logger.error("User saving failed" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> saveTitle(TitleDto titleDto) {
        try{
        Optional<Title> existingTitle = titleRepository.findByName(titleDto.getName());

        if (existingTitle.isPresent()) {
            logger.error("Title Saving Failed, Title Already Exist");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title Already Exist.");
        }

        Title title = new Title();
        title.setName(titleDto.getName());
        titleRepository.save(title);

        logger.info("Title Saved Successful");
        return ResponseEntity.status(201).body("Title Saved Successful");
        
        }catch(Exception exception){
            logger.error("Failed to save Title to the Database, Server Error: " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
