package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.MassRequest;
import org.zelator.entity.User;
import org.zelator.repository.CalendarEventRepository;

import java.time.LocalDate;

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

}
