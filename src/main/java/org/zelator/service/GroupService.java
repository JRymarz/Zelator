package org.zelator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.dto.GroupDetailsDto;
import org.zelator.dto.MysteryDto;
import org.zelator.entity.Group;
import org.zelator.entity.Intention;
import org.zelator.entity.User;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.UserRepository;

import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

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


    public GroupDetailsDto getGroupDetails(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa o podanym id nie istnieje."));

        GroupDetailsDto roseDetails = new GroupDetailsDto();
        roseDetails.setId(group.getId());
        roseDetails.setIntention(group.getIntention());
        roseDetails.setName(group.getName());

        User leader = userRepository.findById(group.getLeader().getId())
                .orElseThrow(() -> new IllegalArgumentException("Taki użytkownik nie istnieje."));

        MysteryDto leaderMyseteryDto = new MysteryDto();
        if(leader.getMystery() != null) {
            leaderMyseteryDto.setId(leader.getMystery().getId());
            leaderMyseteryDto.setName(leader.getMystery().getName());
            leaderMyseteryDto.setMeditation(leader.getMystery().getMeditation());
        }

        GroupDetailsDto.MemberDetailsDto leaderDto = new GroupDetailsDto.MemberDetailsDto();
        leaderDto.setRole(leader.getRole().name());
        leaderDto.setMystery(leaderMyseteryDto);
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

                    MysteryDto memberMysteryDto = new MysteryDto();
                    if(memberDto.getMystery() != null) {
                        memberMysteryDto.setMeditation(member.getMystery().getMeditation());
                        memberMysteryDto.setName(member.getMystery().getName());
                        memberMysteryDto.setId(member.getMystery().getId());
                    }

                    memberDto.setMystery(memberMysteryDto);

                    return memberDto;
                })
                .collect(Collectors.toList());

        roseDetails.setMembers(members);

        return roseDetails;
    }

}
