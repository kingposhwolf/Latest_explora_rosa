package com.example.demo.Controllers.Share;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.SocialMedia.Share.ShareDto;
import com.example.demo.Services.ShareService.ShareServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/share")
@AllArgsConstructor
public class ShareController {
    private final ShareServiceImpl shareService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/add")
    public ResponseEntity<Object> share(@RequestBody @Valid ShareDto shareDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return shareService.shareOperation(shareDto);
    }
}
