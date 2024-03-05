package com.example.demo.Services.SuggestionService;

import com.example.demo.Models.Suggestion;
import com.example.demo.Repositories.SuggestionRepository;
import com.example.demo.Services.CityService.CityServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuggestionServiceImpl implements SuggestionService{

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    private final SuggestionRepository suggestionRepository;

    public SuggestionServiceImpl(SuggestionRepository suggestionRepository){
        this.suggestionRepository = suggestionRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<Suggestion> getAllSuggestions(){
        try{
            Iterable<Suggestion> suggestions = suggestionRepository.findAll();

        if(suggestions.iterator().hasNext()){
            logger.error("\nThere is Request for Fetching All Suggestions, But Nothing Registered to the database ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Suggestion registered in the Database");
        }else{
            logger.info("\nSuccessful fetched all Suggestions");
            return ResponseEntity.status(200).body(suggestions);
        }

        }

    }
}
