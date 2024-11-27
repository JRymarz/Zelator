package org.zelator.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.LoginRequest;
import org.zelator.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            System.out.println(isAuthenticated);
            if(isAuthenticated) {
                session.setAttribute("user", loginRequest.getEmail());
                return ResponseEntity.ok("Zalogowano poprawnie.");
            } else {
                System.out.println("else");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne hasło lub email.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Napotkano nieznany błąd.");
        }
    }
}
