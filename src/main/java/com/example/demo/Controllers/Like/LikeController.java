package com.example.demo.Controllers.Like;

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.LikeDto;
import com.example.demo.Services.LikeService.LikeServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeServiceImpl likeService;

    public LikeController(LikeServiceImpl likeService) {
        this.likeService = likeService;
    }

    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/save")
    public ResponseEntity<Object> writeMessage(@RequestBody @Valid LikeDto likeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return likeService.likeOperation(likeDto);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteMessage(@RequestBody @Valid @NotNull Long likeId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return likeService.deleteLike(likeId);
    }
}
