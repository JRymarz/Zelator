package org.zelator.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.entity.Mystery;
import org.zelator.entity.User;
import org.zelator.service.MysteryService;
import org.zelator.service.PrayerStatusService;
import org.zelator.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class MysteryController {


    private final MysteryService mysteryService;
    private final UserService userService;
    private final PrayerStatusService prayerStatusService;


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
        LocalDate today = LocalDate.now();
        try {
            User user = userService.getById(memberId);
            userService.assignMystery(memberId, mysteryId);
            prayerStatusService.createNewPrayerStatus(user, today);

            return ResponseEntity.ok("Tajemnica została przypisana.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd przypisywania tajemnicy.");
        }
    }


    @GetMapping("/mysteries")
    @CrossOrigin
    public ResponseEntity<List<Mystery>> getAllMysteries() {
        List<Mystery> mysteries = mysteryService.getAllMysteries();
        return ResponseEntity.ok(mysteries);
    }

}
