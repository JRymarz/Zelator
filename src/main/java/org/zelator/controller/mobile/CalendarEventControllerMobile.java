package org.zelator.controller.mobile;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.CalendarEventDto;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.User;
import org.zelator.repository.CalendarEventRepository;
import org.zelator.service.CalendarEventService;
import org.zelator.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CalendarEventControllerMobile {


    private final UserService userService;
    private final CalendarEventRepository calendarEventRepository;
    private final CalendarEventService calendarEventService;


    @GetMapping("/mob/calendar-events")
    @CrossOrigin
    public ResponseEntity<?> getMyEvents(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if (user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            List<CalendarEvent> events = calendarEventRepository.findEventsForMember(user.getId(), user.getGroup().getId());

            List<CalendarEventDto> eventDtos = events.stream()
                    .map(event -> new CalendarEventDto(
                            event.getId(),
                            event.getTitle(),
                            event.getEventDate(),
                            event.getEventType().name(),
                            event.getCreator().getFirstName() + " " + event.getCreator().getLastName()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(eventDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }


    @PostMapping("/mob/calendar-events/create")
    @CrossOrigin
    public ResponseEntity<?> createEvent(@RequestBody CalendarEventDto eventDto,
                                         @RequestHeader("User-ID") Long userId) {

        try {
            User user = userService.getById(userId);
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

            calendarEventService.createOtherEvent(eventDto, user);
            return ResponseEntity.ok("Event created succesfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }

}
