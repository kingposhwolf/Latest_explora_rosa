package com.example.demo.Controllers.HashTag;
import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.SocialMedia.HashTag.HashTagDto;
import com.example.demo.InputDto.SocialMedia.HashTag.PostsHashTag;
import com.example.demo.Services.HashTagService.HashTagServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hashTags")
@AllArgsConstructor
public class HashTagController {

    private GlobalValidationFormatter globalValidationFormatter;

    private final HashTagServiceImpl hashTagServiceImpl;


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

    @PostMapping("/posts")
    public ResponseEntity<Object> getPostsHashTag(@RequestBody @Valid PostsHashTag hashTagDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return hashTagServiceImpl.getPostsHashTags(hashTagDto);
    }
}
