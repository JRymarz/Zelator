package org.zelator.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.entity.Mystery;
import org.zelator.service.MysteryService;
import org.zelator.service.UserService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class MysteryController {


    private final MysteryService mysteryService;
    private final UserService userService;


    @GetMapping("/roses/{roseId}/available-mysteries")
    @CrossOrigin
    public ResponseEntity<List<Mystery>> getAvailableMysteries(@PathVariable Long roseId) {
        List<Mystery> availableMysteries = mysteryService.getAvailableMysteries(roseId);
        return ResponseEntity.ok(availableMysteries);
    }


    @PatchMapping("/members/{memberId}/assign-mystery")
    @CrossOrigin
    public ResponseEntity<?> assignMysteryToMember(@PathVariable Long memberId, @RequestParam Long mysteryId) {
        System.out.println("MemberId: " + memberId + ", mysteryId: " + mysteryId);
        try {
            userService.assignMystery(memberId, mysteryId);
            return ResponseEntity.ok("Tajemnica została przypisana.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd przypisywania tajemnicy.");
        }
    }

}
