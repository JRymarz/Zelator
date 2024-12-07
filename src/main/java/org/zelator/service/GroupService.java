package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.Group;
import org.zelator.entity.Intention;
import org.zelator.entity.User;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.UserRepository;

import java.util.SplittableRandom;

@Service
@RequiredArgsConstructor
public class GroupService {


    private final GroupRepository groupRepository;

    private final UserRepository userRepository;


    public Group createGroup(String name, User leader, Intention intention) {
        if(leader.getRole() != User.Role.Zelator) {
            throw new IllegalArgumentException("Tylko Zelator może utworzyć różę.");
        }

        if(leader.getGroup() != null) {
            throw new IllegalArgumentException("Zelator może zarządzać tylko jedną różą.");
        }

        Group group = new Group();
        group.setName(name);
        group.setLeader(leader);
        group.setIntention(intention);

        leader.setGroup(group);

        group.getMembers().add(leader);

        System.out.println("Grupa utworzona ale przed zapisaniem jej i zmian w leaderze");

        groupRepository.save(group);
        userRepository.save(leader);

        return group;
    }

}
