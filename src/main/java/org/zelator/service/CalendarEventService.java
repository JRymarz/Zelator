package org.zelator.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.zelator.dto.CalendarEventDto;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.MassRequest;
import org.zelator.entity.User;
import org.zelator.repository.CalendarEventRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarEventService {


    private final CalendarEventRepository calendarEventRepository;


    public void createMissedPrayerEvent(User user, LocalDate missedPrayerDate) {
        CalendarEvent calendarEvent = new CalendarEvent();

        calendarEvent.setTitle("Niedmówiona modlitwa");
        calendarEvent.setEventDate(missedPrayerDate.atStartOfDay());
        calendarEvent.setEventType(CalendarEvent.EventType.PRAYER);
        calendarEvent.setCreator(user);
        calendarEvent.setState(CalendarEvent.State.undone);

        calendarEventRepository.save(calendarEvent);
    }


    public void createMassEvent(MassRequest massRequest) {
        CalendarEvent event = new CalendarEvent();
        event.setTitle("Msza Święta: " + massRequest.getIntention());
        event.setEventDate(massRequest.getMassDate());
        event.setCreator(massRequest.getUser());
        event.setEventType(CalendarEvent.EventType.MASS);
        event.setState(CalendarEvent.State.scheduled);

        calendarEventRepository.save(event);
    }


    public void createOtherEvent(CalendarEventDto eventDto, User user) {
        CalendarEvent event = new CalendarEvent();
        event.setTitle(eventDto.getTitle());
        event.setEventDate(eventDto.getEventDate());
        event.setEventType(CalendarEvent.EventType.OTHER);
        event.setCreator(user);
        event.setState(CalendarEvent.State.scheduled);

        calendarEventRepository.save(event);
    }

}
