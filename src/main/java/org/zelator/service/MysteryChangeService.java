package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.*;
import org.zelator.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
                                  boolean autoAssign) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono grupy."));

        Intention intention = intentionRepository.findById(intentionId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono intencji."));

        MysteryChangeTask task = new MysteryChangeTask();
        task.setGroup(group);
        task.setIntention(intention);
        task.setState(MysteryChangeTask.State.PENDING);
        task.setEventDate(eventDate);

        if(autoAssign) {
            List<User> members = userRepository.findByGroupId(group.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkowników."));

            List<Mystery> availableMysteries = mysteryRepository.findAll();

            List<Mystery> assignedMysteries = assignMysteriesToMembers(members, availableMysteries);

            for(int i = 0; i < members.size(); i++) {
                MysteryChangeTaskMember taskMember = new MysteryChangeTaskMember();
                taskMember.setMysteryChangeTask(task);
                taskMember.setUser(members.get(i));
                taskMember.setMystery(assignedMysteries.get(i));
                task.getTaskMembers().add(taskMember);
            }
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
        mysteryChangeTaskRepository.save(task);
    }


    private List<Mystery> assignMysteriesToMembers(List<User> members, List<Mystery> availableMysteries) {
        Collections.shuffle(availableMysteries);
        List<Mystery> assignedMysteries = new ArrayList<>();
        for(int i = 0; i < members.size(); i++) {
            assignedMysteries.add(availableMysteries.get(i));
        }

        return assignedMysteries;
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
