package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.CalendarEvent;
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

}
