package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.zelator.entity.*;
import org.zelator.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MysteryChangeService {


    private final MysteryRepository mysteryRepository;
    private final UserRepository userRepository;
    private final MysteryChangeTaskRepository mysteryChangeTaskRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final GroupRepository groupRepository;
    private final IntentionRepository intentionRepository;
    private final MysteryChangeTaskMemberRepository mysteryChangeTaskMemberRepository;


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
        List<Mystery> availableMysteries = mysteryRepository.findAll();
        if(availableMysteries.size() != 20)
            throw new IllegalArgumentException("Błędna ilość tajemnic.");

        Collections.shuffle(availableMysteries);

        Set<Mystery> usedMysteries = new HashSet<>();

        for(User member : members) {
            Optional<Mystery> availableMystery = availableMysteries.stream()
                    .filter(m -> !m.equals(member.getMystery()) && !usedMysteries.contains(m))
                    .findFirst();

            if(availableMystery.isPresent()) {
                Mystery assignedMystery = availableMystery.get();
                MysteryChangeTaskMember taskMember = new MysteryChangeTaskMember();
                taskMember.setMysteryChangeTask(task);
                taskMember.setUser(member);
                taskMember.setMystery(assignedMystery);

                usedMysteries.add(assignedMystery);
                task.getTaskMembers().add(taskMember);
            } else {
                System.err.println("Brak dostępnych tajemnic.");
            }
        }
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


    @Transactional
    public void performMysteryChange(MysteryChangeTask task) {
        System.out.println("Wykonuje zmiane tajemnic dla grupy " + task.getGroup().getId());

        Group group = task.getGroup();
        Intention intention = task.getIntention();
        group.setIntention(intention);

        CalendarEvent calendarEvent = task.getCalendarEvent();
        if(calendarEvent != null) {
            calendarEvent.setState(CalendarEvent.State.completed);
            System.out.println("Zmieniono stan calendarEvent");
        } else
            System.err.println("Nie znaleziono calendarEvent.");

        List<MysteryChangeTaskMember> taskMembers = task.getTaskMembers();
        for(MysteryChangeTaskMember taskMember : taskMembers) {
            User user = taskMember.getUser();
            Mystery mystery = taskMember.getMystery();

            if(user != null && mystery != null) {
                user.setMystery(mystery);
                System.out.println("Przypisano mystery userowi.");
            } else
                System.err.println("Brakuje danych.");
        }

        try{
            groupRepository.save(group);

            if(calendarEvent != null) {
                calendarEventRepository.save(calendarEvent);
            }

            userRepository.saveAll(
                    taskMembers.stream()
                            .map(MysteryChangeTaskMember::getUser)
                            .collect(Collectors.toList())
            );
            System.out.println("Wszystkie zmiany zapisane w bazie.");
        } catch (Exception e) {
            System.err.println("Bład podczas zapisywania w bazie.");
            throw new RuntimeException("Nie udało się zapisać zmian.");
        }
    }


}
