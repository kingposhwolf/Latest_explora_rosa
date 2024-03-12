package com.example.demo.Controllers.Like;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.LikeDto;
import com.example.demo.Services.LikeService.LikeServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeServiceImpl likeService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/save")
    public ResponseEntity<Object> writeMessage(@RequestBody @Valid LikeDto likeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return likeService.likeOperation(likeDto);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> fetchLikes(@RequestBody @Valid @NotNull Long postId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return likeService.fetchLikes(postId);
    }
}
