package org.zelator.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zelator.entity.User;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
public class HomeController {


    @GetMapping("/")
    @CrossOrigin
    public ResponseEntity<?> getHome(HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś zalogowany.");
        }

        System.out.println(user.getRole());

        return ResponseEntity.ok(user);
    }

}
