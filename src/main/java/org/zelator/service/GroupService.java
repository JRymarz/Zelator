package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.dto.GroupDetailsDto;
import org.zelator.entity.Group;
import org.zelator.entity.Intention;
import org.zelator.entity.Mystery;
import org.zelator.entity.User;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.MysteryRepository;
import org.zelator.repository.UserRepository;

import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {


    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final MysteryRepository mysteryRepository;


    public Group createGroup(String name, User leader, Intention intention) {

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


    public GroupDetailsDto getGroupDetails(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa o podanym id nie istnieje."));

        GroupDetailsDto roseDetails = new GroupDetailsDto();
        roseDetails.setId(group.getId());
        roseDetails.setIntention(group.getIntention());
        roseDetails.setName(group.getName());

        User leader = userRepository.findById(group.getLeader().getId())
                .orElseThrow(() -> new IllegalArgumentException("Taki użytkownik nie istnieje."));

        Mystery leaderMystery = new Mystery();
        if(leader.getMystery() != null) {
            leaderMystery = leader.getMystery();
        }

        GroupDetailsDto.MemberDetailsDto leaderDto = new GroupDetailsDto.MemberDetailsDto();
        leaderDto.setRole(leader.getRole().name());
        leaderDto.setMystery(leaderMystery);
        leaderDto.setEmail(leader.getEmail());
        leaderDto.setFirstName(leader.getFirstName());
        leaderDto.setId(leader.getId());
        leaderDto.setLastName(leader.getLastName());

        roseDetails.setLeader(leaderDto);

        List<GroupDetailsDto.MemberDetailsDto> members = group.getMembers().stream()
                .map(member -> {
                    GroupDetailsDto.MemberDetailsDto memberDto = new GroupDetailsDto.MemberDetailsDto();
                    memberDto.setLastName(member.getLastName());
                    memberDto.setRole(member.getRole().name());
                    memberDto.setEmail(member.getEmail());
                    memberDto.setId(member.getId());
                    memberDto.setFirstName(member.getFirstName());

                    Mystery memberMysteryDto = new Mystery();
                    if(member.getMystery() != null) {
                        memberMysteryDto = member.getMystery();
                    }

                    memberDto.setMystery(memberMysteryDto);

                    return memberDto;
                })
                .collect(Collectors.toList());

        roseDetails.setMembers(members);

        return roseDetails;
    }

}
