package org.zelator.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.dto.GroupDetailsDto;
import org.zelator.dto.GroupRequest;
import org.zelator.dto.SimpleGroupDetailsDto;
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


    public Group createGroup(String name, Intention intention) {
        Group group = new Group();
        group.setName(name);
        group.setIntention(intention);

        groupRepository.save(group);

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


    public List<GroupRequest> getAllGroupList() {
        return groupRepository.findAll().stream()
                .map(group -> {
                    GroupRequest dto = new GroupRequest();
                    dto.setGroupId(group.getId());
                    dto.setName(group.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public SimpleGroupDetailsDto getSimpleGroupDetails(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa o podanym id nie istnieje."));

        SimpleGroupDetailsDto groupDetails = new SimpleGroupDetailsDto();
        groupDetails.setId(group.getId());
        groupDetails.setName(group.getName());

        // Sprawdzenie, czy leader istnieje
        GroupDetailsDto.MemberDetailsDto leaderDto = null;
        if (group.getLeader() != null) {
            User leader = userRepository.findById(group.getLeader().getId())
                    .orElse(null);

            if (leader != null) {
                leaderDto = new GroupDetailsDto.MemberDetailsDto();
                leaderDto.setId(leader.getId());
                leaderDto.setFirstName(leader.getFirstName());
                leaderDto.setLastName(leader.getLastName());
                leaderDto.setEmail(leader.getEmail());
                leaderDto.setRole(leader.getRole().name());
            }
        }
        groupDetails.setLeader(leaderDto);

        List<GroupDetailsDto.MemberDetailsDto> members = group.getMembers().stream()
                .map(member -> {
                    GroupDetailsDto.MemberDetailsDto memberDto = new GroupDetailsDto.MemberDetailsDto();
                    memberDto.setId(member.getId());
                    memberDto.setFirstName(member.getFirstName());
                    memberDto.setLastName(member.getLastName());
                    memberDto.setEmail(member.getEmail());
                    memberDto.setRole(member.getRole().name());
                    return memberDto;
                })
                .collect(Collectors.toList());

        groupDetails.setMembers(members);

        return groupDetails;
    }


    @Transactional
    public void appointLeader(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa nie istnieje"));

        User currentLeader = group.getLeader();
        if(currentLeader != null){
            currentLeader.setRole(User.Role.Member);
            userRepository.save(currentLeader);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Uzytkownik nie istnieje"));

        group.setLeader(user);
        user.setRole(User.Role.Zelator);

        groupRepository.save(group);
        userRepository.save(user);
    }

}
