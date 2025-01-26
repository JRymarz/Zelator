package org.zelator.controller.mobile;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.entity.MassRequest;
import org.zelator.entity.User;
import org.zelator.repository.MassRequestRepository;
import org.zelator.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/mob")
public class MassRequestControllerMobile {
    
    
    private final UserService userService;
    private final MassRequestRepository massRequestRepository;


    @PostMapping("/mass-request")
    @CrossOrigin
    public ResponseEntity<?> createMassRequest(@RequestHeader("User-ID") Long userId,
                                               @RequestBody MassRequest massRequest) {

        try{
            User user = userService.getById(userId);
            if(user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");

            massRequest.setUser(user);
            massRequest.setStatus(MassRequest.MassStatus.PENDING);
            massRequest.setRequestDate(LocalDate.now());

            massRequestRepository.save(massRequest);

            return ResponseEntity.ok("Prośba o Mszę Świętą została utworzona.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }


    @GetMapping("/mass-requests")
    @CrossOrigin
    public ResponseEntity<?> getMyMasses(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");

            List<MassRequest> massRequests = massRequestRepository.findByUser(user).orElse(null);
            boolean hasGroup = user.getGroup() != null;

            Map<String, Object> response = new HashMap<>();
            response.put("hasGroup", hasGroup);

            for(MassRequest massRequest : massRequests) {
                massRequest.setUser(null);
            }

            if(massRequests == null || massRequests.isEmpty()) {
                response.put("requests", massRequests);
                return ResponseEntity.ok(response);
            }

            response.put("requests", massRequests);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }

}
