package com.example.demo.Controllers;

import com.example.demo.Dto.HashTagDto;
import com.example.demo.Services.HashTagService.HashTagServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hashTags")
public class HashTagController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final HashTagServiceImpl hashTagServiceImpl;

    public HashTagController(HashTagServiceImpl hashTagServiceImpl) {
        this.hashTagServiceImpl = hashTagServiceImpl;
    }

    @PostMapping("/all")
    public ResponseEntity<Object> getAllHashTags() {
        return hashTagServiceImpl.getAllHashTags();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCountry(@RequestBody @Valid HashTagDto hashTagDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return hashTagServiceImpl.saveHashTag(hashTagDto);
    }

    @PostMapping("/{name}")
    public ResponseEntity<Object> getHashTagByName(@PathVariable String name) {
        return hashTagServiceImpl.getHashTagByName(name);
    }
}
