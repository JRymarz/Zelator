package org.zelator.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zelator.service.MysteryChangeService;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class MysteryChangeController {


    private final MysteryChangeService mysteryChangeService;


    @PostMapping("/mystery-change/plan")
    public ResponseEntity<String> planMysteryChange(@RequestParam Long groupId,
                                                    @RequestParam Long intentionId,
                                                    @RequestParam LocalDateTime eventDate,
                                                    @RequestParam boolean autoAssign) {

        try {
            mysteryChangeService.planMysteryChange(groupId, intentionId, eventDate, autoAssign);
            return ResponseEntity.ok("Zmiana tajemnic została zaplanowana.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd przy planowaniu zmiany tajemnic: " + e.getMessage());
        }
    }

}
