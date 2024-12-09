package org.zelator.controller.web;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.GroupDetailsDto;
import org.zelator.dto.GroupRequest;
import org.zelator.entity.Intention;
import org.zelator.entity.User;
import org.zelator.service.GroupService;
import org.zelator.service.IntentionService;
import org.zelator.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GroupController {


    private final GroupService groupService;

    private final IntentionService intentionService;

    private final UserService userService;


    @PostMapping("/groups/create")
    @PreAuthorize("hasAuthority('Zelator')")
    @CrossOrigin
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest groupRequest) {
        System.out.println("Jestem w backendzie");
        try {
            User leader = userService.getCurrentUser();
            Intention intention = intentionService.getById(groupRequest.getIntentionId());
            System.out.println(leader.getEmail() + ", " + intention.getTitle());

            groupService.createGroup(groupRequest.getName(), leader, intention);

            return ResponseEntity.ok("Róża zostałą utworzona pomyślnie.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie udało się utworzyć nowej róży.");
        }
    }


    @GetMapping("/my-rose")
    @CrossOrigin
    public ResponseEntity<?> getMyRoseDetails(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś zalogowany.");
        } else if (user.getGroup() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie masz własnej grupy.");
        }

        GroupDetailsDto roseDetails = groupService.getGroupDetails(user.getGroup().getId());

        return ResponseEntity.ok(roseDetails);
    }


}
