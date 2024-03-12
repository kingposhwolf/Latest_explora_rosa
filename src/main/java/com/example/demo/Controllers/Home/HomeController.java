package com.example.demo.Controllers.Home;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class HomeController {
    @GetMapping("/home")
    public String getHome() {
        return "Hello World";
    }

    @PostMapping("/api/test")
    public String postMethodName(@RequestBody String name) {
        
        return name;
    }
    
    
}
