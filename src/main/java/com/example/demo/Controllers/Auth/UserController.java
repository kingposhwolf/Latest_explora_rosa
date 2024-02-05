package com.example.demo.Controllers.Auth;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.User;
import com.example.demo.Services.UserSerives.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult){

        //This part will be executed only if there is validation errors.
        if(bindingResult.hasErrors()){
            //Map All available errors into lists for output
            List<String> errors = bindingResult.getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

}
