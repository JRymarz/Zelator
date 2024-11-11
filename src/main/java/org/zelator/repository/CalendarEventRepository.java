package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {



}
