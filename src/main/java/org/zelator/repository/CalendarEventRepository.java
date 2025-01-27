package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zelator.entity.CalendarEvent;

import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {


    @Query("SELECT e FROM CalendarEvent e " +
            "WHERE e.creator.id = :creatorId " +
            "OR (e.eventType = 'PRAYER' AND e.creator.group.id = :groupId)")
    List<CalendarEvent> findEventsForZelator(@Param("creatorId") Long creatorId, @Param("groupId") Long groupId);


    @Query("SELECT e FROM CalendarEvent e " +
            "WHERE e.creator.id = :creatorId " +
            "OR e.group.id = :groupId")
    List<CalendarEvent> findEventsForMember(@Param("creatorId") Long creatorId, @Param("groupId") Long groupId);

}
