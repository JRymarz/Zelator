package org.zelator.controller.mobile;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.LoginRequest;
import org.zelator.entity.Group;
import org.zelator.entity.Intention;
import org.zelator.entity.Mystery;
import org.zelator.entity.User;
import org.zelator.service.IntentionService;
import org.zelator.service.MysteryService;
import org.zelator.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserControllerMobile {


    private final UserService userService;
    private final IntentionService intentionService;
    private final MysteryService mysteryService;


    @PostMapping("/mob/login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        User user = userService.getByEmail(loginRequest.getEmail());
        System.out.println(isAuthenticated);

        if(isAuthenticated) {
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, AuthorityUtils.createAuthorityList(user.getRole().name()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(user.getEmail());

//            session.setAttribute("user", user);

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("role", user.getRole().name());
            response.put("email", user.getEmail());
            if(user.getGroup() == null) {
                response.put("group", null);
            } else
                response.put("group", user.getGroup().getId());

            System.out.println("Wysylam dane usera do frontu");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowe dane logowania.");
        }
    }


    @GetMapping("/mob/intention-mystery")
    @CrossOrigin
    public ResponseEntity<?> getInentionAndMystery(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");
            }

            Group group = user.getGroup();
            Intention intention = (group != null) ? group.getIntention() : null;
            Mystery mystery = user.getMystery();

            Map<String, Object> response = new HashMap<>();
            response.put("intention", intention != null ? Map.of("title", intention.getTitle()) : null);
            response.put("mystery", mystery != null ? Map.of("name", mystery.getName()) : null);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd: " + e.getMessage());
        }
    }

}
