package org.zelator.controller.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.CalendarEventDto;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.User;
import org.zelator.repository.CalendarEventRepository;
import org.zelator.service.CalendarEventService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CalendarEventController {


    private final CalendarEventRepository calendarEventRepository;
    private final CalendarEventService calendarEventService;


    @GetMapping("/calendar-events")
    @CrossOrigin
    public ResponseEntity<List<CalendarEventDto>> getCalendarEventForZelator(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getGroup() == null)
            return ResponseEntity.badRequest().build();

        List<CalendarEvent> events = calendarEventRepository.findEventsForZelator(user.getId(), user.getGroup().getId());

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
    }


    @PostMapping("/calendar-events/create")
    @CrossOrigin
    public ResponseEntity<?> createEvent(@RequestBody CalendarEventDto eventDto,
                                         HttpSession session) {

        User user = (User) session.getAttribute("user");
        if(user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");

        System.out.println(eventDto);
        try {
            calendarEventService.createOtherEvent(eventDto, user);
            return ResponseEntity.ok("Event created succesfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwny błąd");
        }
    }


    @GetMapping("/calendar-events/next")
    @CrossOrigin
    public ResponseEntity<?> getNextCalendarEvent(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if(user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            List<CalendarEvent> events = calendarEventRepository.findEventsForZelator(user.getId(), user.getGroup().getId());

            List<CalendarEvent> upcomingEvents = events.stream()
                    .filter(event -> event.getEventDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(CalendarEvent::getEventDate))
                    .collect(Collectors.toList());

            if(upcomingEvents.isEmpty())
                return ResponseEntity.ok(null);

            CalendarEvent nextEvent = upcomingEvents.get(0);
            CalendarEventDto eventDto = new CalendarEventDto(
                    nextEvent.getId(),
                    nextEvent.getTitle(),
                    nextEvent.getEventDate(),
                    nextEvent.getEventType().name(),
                    nextEvent.getCreator().getFirstName() + " " + nextEvent.getCreator().getLastName()
            );

            return ResponseEntity.ok(eventDto);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }

}
