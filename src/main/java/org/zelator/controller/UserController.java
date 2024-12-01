package org.zelator.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.LoginRequest;
import org.zelator.entity.User;
import org.zelator.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            User user = userService.getByEmail(loginRequest.getEmail());

            System.out.println(isAuthenticated);
            if(isAuthenticated) {
                session.setAttribute("user", user);
                System.out.println(user.getEmail());
                return ResponseEntity.ok("Zalogowano poprawnie.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne hasło lub email.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Napotkano nieznany błąd.");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println("logout");
        session.invalidate();
        return ResponseEntity.ok("Wylogowano pomyślnie.");
    }


    @GetMapping("/current-user")
    @CrossOrigin
    public ResponseEntity<?> getCurrentUser(HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null) {
            System.out.println("No user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś zalogowany.");
        }

        System.out.println(user.getRole());

        return ResponseEntity.ok(user);
    }

}
