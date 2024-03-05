package com.example.demo.Services.SuggestionService;

import com.example.demo.Dto.SuggestionDto;
import com.example.demo.Models.Suggestion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SuggestionService {
    ResponseEntity<Suggestion> getAllSuggestions();

    ResponseEntity<Object> saveSuggestion(SuggestionDto suggestionDto);
}
