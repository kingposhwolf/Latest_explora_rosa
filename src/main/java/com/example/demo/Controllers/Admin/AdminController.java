package com.example.demo.Controllers.Admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/")
@AllArgsConstructor
public class AdminController {
    
    @GetMapping("/dashboard")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello Admin");
    }
}
