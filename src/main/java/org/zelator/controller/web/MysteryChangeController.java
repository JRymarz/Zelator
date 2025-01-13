package org.zelator.controller.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.MysteryChangeRequest;
import org.zelator.service.MysteryChangeService;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class MysteryChangeController {


    private final MysteryChangeService mysteryChangeService;


    @PostMapping("/mystery-change/plan")
    public ResponseEntity<String> planMysteryChange(@Valid @RequestBody MysteryChangeRequest request) {

        try {
            mysteryChangeService.planMysteryChange(
                    request.getGroupId(),
                    request.getIntentionId(),
                    request.getEventDate(),
                    request.isAutoAssign(),
                    request.getMemberMysteries()
            );

            return ResponseEntity.ok("Zmiana tajemnic została zaplanowana.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd przy planowaniu zmiany tajemnic: " + e.getMessage());
        }
    }

}
