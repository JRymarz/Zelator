package org.zelator.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zelator.entity.Intention;
import org.zelator.service.IntentionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
public class IntentionController {


    private final IntentionService intentionService;


    @GetMapping("/intentions")
    @CrossOrigin
    @PreAuthorize("hasAuthority('Zelator')")
    public List<Intention> getAllIntentions() {
        return intentionService.getAllIntentions();
    }

}
