package org.zelator.controller.web;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.MassRequestDto;
import org.zelator.entity.Group;
import org.zelator.entity.MassRequest;
import org.zelator.entity.User;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.MassRequestRepository;
import org.zelator.service.MassRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RequiredArgsConstructor
public class MassRequestController {


    private final MassRequestRepository massRequestRepository;
    private final GroupRepository groupRepository;
    private final MassRequestService massRequestService;


    @GetMapping("/mass-requests")
    @CrossOrigin
    public ResponseEntity<?> getMyRequests(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś zalogowany.");
            } else if (user.getGroup() == null) {
                return ResponseEntity.ok(null);
            }

            System.out.println("Jestem zalogowany i mam grupe.");

            Group group = user.getGroup();

            List<User> groupMembers = groupRepository.findMembersByGroupId(group.getId());
            System.out.println("Mam memberow.");
            for(User member : groupMembers) {
                System.out.println(member.getId());
            }
            List<Long> membersId = groupMembers.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            System.out.println("Mam idiki memberow.");

            List<MassRequest> massRequests = massRequestRepository.findByUserIdIn(membersId);

            if(massRequests.isEmpty()) {
                return ResponseEntity.ok(null);
            }
            System.out.println("Mam liste requestow.");

            List<MassRequestDto> massRequestDtos = massRequests.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            System.out.println("Mam liste dto do wyslania");

            return ResponseEntity.ok(massRequestDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił nieoczekiwany błąd.");
        }
    }


    private MassRequestDto mapToDto(MassRequest massRequest) {
        MassRequestDto dto= new MassRequestDto();
        dto.setId(massRequest.getId());
        dto.setIntention(massRequest.getIntention());
        dto.setRequestDate(massRequest.getRequestDate());
        dto.setMassDate(massRequest.getMassDate());
        dto.setStatus(massRequest.getStatus().name());

        MassRequestDto.AuthorDto authorDto = new MassRequestDto.AuthorDto();
        authorDto.setId(massRequest.getUser().getId());
        authorDto.setFirstName(massRequest.getUser().getFirstName());
        authorDto.setLastName(massRequest.getUser().getLastName());
        dto.setUser(authorDto);

        return dto;
    }


    @PostMapping("/mass-requests/{id}/approve")
    @CrossOrigin
    public ResponseEntity<?> approveMassRequestStatus(@PathVariable Long id) {
        try {
            massRequestService.approveRequest(id);
            return ResponseEntity.ok("Request approved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }


    @PostMapping("/mass-requests/{id}/reject")
    @CrossOrigin
    public ResponseEntity<?> rejectMassRequest(@PathVariable Long id) {
        try {
            massRequestService.rejectRequest(id);
            return ResponseEntity.ok("Request rejected.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }


    @GetMapping("/mass-request/are-unchecked")
    @CrossOrigin
    public ResponseEntity<?> areUnchecked(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || user.getGroup() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            List<User> groupMembers = groupRepository.findMembersByGroupId(user.getGroup().getId());
            List<Long> membersId = groupMembers.stream().map(User::getId).collect(Collectors.toList());

            Boolean response = massRequestRepository.existsByUserIdInAndStatus(membersId, MassRequest.MassStatus.PENDING);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił nieoczekiwany błąd.");
        }
    }

}
