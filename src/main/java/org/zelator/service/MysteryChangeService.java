package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.*;
import org.zelator.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MysteryChangeService {


    private final MysteryRepository mysteryRepository;
    private final UserRepository userRepository;
    private final MysteryChangeTaskRepository mysteryChangeTaskRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final GroupRepository groupRepository;
    private final IntentionRepository intentionRepository;


    public void planMysteryChange(Long groupId,
                                  Long intentionId,
                                  LocalDateTime eventDate,
                                  boolean autoAssign,
                                  Map<Long, Long> memberMysteries) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono grupy."));

        Intention intention = intentionRepository.findById(intentionId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono intencji."));

        MysteryChangeTask task = new MysteryChangeTask();
        task.setGroup(group);
        task.setIntention(intention);
        task.setState(MysteryChangeTask.State.PENDING);
        task.setEventDate(eventDate);
        task.setTaskMembers(new ArrayList<>());

        List<User> members = userRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znialeziono członków grupy."));

        if(autoAssign) {
            assignMysteriesAutomatically(task, members);
        } else {
            assignMysteriesManually(task, members, memberMysteries);
        }

        CalendarEvent event = new CalendarEvent();
        event.setTitle("Zmiana tajemnic różańcowych");
        event.setEventDate(eventDate);
        event.setEventType(CalendarEvent.EventType.MYSTERYCHANGE);
        event.setGroup(group);
        event.setCreator(group.getLeader());
        event.setState(CalendarEvent.State.scheduled);

        event.setMysteryChangeTask(task);
        calendarEventRepository.save(event);

        task.setCalendarEvent(event);
        mysteryChangeTaskRepository.save(task);
    }


    private void assignMysteriesAutomatically(MysteryChangeTask task, List<User> members) {
        List<Mystery> mysteries = mysteryRepository.findAll();
        System.out.println("TODO automatyczne przypisanie tajemnic");
    }


    private void assignMysteriesManually(MysteryChangeTask task, List<User> members, Map<Long, Long> memberMysteries) {
        for(User member : members) {
            if(memberMysteries.containsKey(member.getId())) {
                MysteryChangeTaskMember taskMember = new MysteryChangeTaskMember();
                taskMember.setMysteryChangeTask(task);
                taskMember.setUser(member);
                Mystery mystery = mysteryRepository.findById(memberMysteries.get(member.getId()))
                        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono tajemnicy."));
                taskMember.setMystery(mystery);
                task.getTaskMembers().add(taskMember);
            }
        }
    }


    public void updateMysteryForMember(Long taskId, Long memberId, Long newMysteryId) {
        MysteryChangeTask task = mysteryChangeTaskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zmiany tajemnic."));

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

        Mystery newMystery = mysteryRepository.findById(newMysteryId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono tajemnicy."));

        MysteryChangeTaskMember taskMember = task.getTaskMembers().stream()
                .filter(tm -> tm.getUser().equals(member))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));
        taskMember.setMystery(newMystery);

        mysteryChangeTaskRepository.save(task);
    }


}
