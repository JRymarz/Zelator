package org.zelator.controller.mobile;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.PrayerReminder;
import org.zelator.entity.*;
import org.zelator.repository.PrayerStatusRepository;
import org.zelator.service.IntentionService;
import org.zelator.service.MysteryService;
import org.zelator.service.PrayerStatusService;
import org.zelator.service.UserService;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PrayerControllerMobile {


    private final IntentionService intentionService;
    private final MysteryService mysteryService;
    private final UserService userService;
    private final PrayerStatusService prayerStatusService;
    private final PrayerStatusRepository prayerStatusRepository;


    @GetMapping("/mob/prayer-details")
    @CrossOrigin
    public ResponseEntity<?> getPrayerDetails(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");
            }

            Group group = user.getGroup();
            Intention intention = null;
            if(group != null)
                intention = user.getGroup().getIntention();

            Mystery mystery = user.getMystery();

            Map<String, Object> response = new HashMap<>();
            response.put("intention", intention);
            response.put("mystery", mystery);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd: " + e.getMessage());
        }
    }


    @PostMapping("/mob/prayer-complete")
    @CrossOrigin
    public ResponseEntity<?> completePrayer(@RequestHeader("User-ID") Long userId) {
        try{
            User user = userService.getById(userId);
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");

            prayerStatusService.markPrayerAsComleted(user);
            return ResponseEntity.ok("Modlitwa oznaczona jako zakończona.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Niespodziewany błąd.");
        }
    }


    @GetMapping("/mob/prayer-status")
    @CrossOrigin
    public ResponseEntity<?> getPrayerStatus(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");
            }

            PrayerStatus status = prayerStatusService.getByUserId(user.getId());
            if(status == null) {
                return ResponseEntity.ok(null);
            }

            return ResponseEntity.ok(status.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczkiwany błąd.");
        }
    }


    @PostMapping("/mob/save-prayer-reminder")
    @CrossOrigin
    public ResponseEntity<?> saveReminderTime(@RequestHeader("User-ID") Long userId,
                                              @RequestBody PrayerReminder reminderRequest) {

        try {
            User user = userService.getById(userId);
            if(user == null || user.getGroup() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");
            }

            PrayerStatus prayerStatus = prayerStatusService.getByUserId(user.getId());
            if(prayerStatus == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");

            LocalTime reminderTime = LocalTime.parse(reminderRequest.getTime());

            prayerStatus.setPrayerReminderTime(reminderTime);

            prayerStatusRepository.save(prayerStatus);

            return ResponseEntity.ok("Czas przypomnienia o modlitwie został zapisany.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania przypomnienia.");
        }
    }

}
