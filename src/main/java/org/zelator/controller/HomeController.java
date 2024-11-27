package org.zelator.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class HomeController {


    @GetMapping("/")
    @CrossOrigin
    public String getWelcome(){
        return "Welcome from REST_API";
    }
}
