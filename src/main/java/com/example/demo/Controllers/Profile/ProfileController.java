package com.example.demo.Controllers.Profile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/personal")
@AllArgsConstructor
public class ProfileController {
    private GlobalValidationFormatter globalValidationFormatter;
}
