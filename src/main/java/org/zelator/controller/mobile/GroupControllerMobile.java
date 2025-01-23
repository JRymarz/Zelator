package org.zelator.controller.mobile;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.zelator.dto.GroupDetailsDto;
import org.zelator.entity.User;
import org.zelator.service.GroupService;
import org.zelator.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class GroupControllerMobile {


    private final GroupService groupService;
    private final UserService userService;


    @GetMapping("/mob/my-rose")
    @CrossOrigin
    public ResponseEntity<?> getMyRoseDetails(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika.");
            } else if(user.getGroup() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie masz własnej grupy.");
            }

            GroupDetailsDto groupDetails = groupService.getGroupDetails(user.getGroup().getId());
            return ResponseEntity.ok(groupDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd: " + e.getMessage());
        }
    }

}
