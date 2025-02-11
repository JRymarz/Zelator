package org.zelator.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.UserDto;
import org.zelator.service.GroupService;
import org.zelator.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RequiredArgsConstructor
public class ZelatorController {


    private final UserService userService;
    private final GroupService groupService;


    @PostMapping("/create-zelator")
    @PreAuthorize("hasAuthority('MainZelator')")
    @CrossOrigin
    public ResponseEntity<String> createZelator(@RequestBody UserDto userDto) {
        try {
            userService.createZelator(userDto);
            return ResponseEntity.ok("Konto Zelatora zostało utworzone.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie udało się utworzyć konta.");
        }
    }


    @PostMapping("/group/{groupId}/appoint-leader/{userId}")
    @CrossOrigin
    public ResponseEntity<?> appointLeader(
            @PathVariable Long userId,
            @PathVariable Long groupId) {

        try {
            groupService.appointLeader(groupId, userId);
            return ResponseEntity.ok("Lider został zmieniony.");
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany blad");
        }
    }

}
