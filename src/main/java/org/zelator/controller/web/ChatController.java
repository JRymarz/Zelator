package org.zelator.controller.web;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zelator.entity.Chat;
import org.zelator.entity.User;
import org.zelator.repository.ChatRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {


    private final ChatRepository chatRepository;


    @GetMapping("/chat/messages")
    @CrossOrigin
    public ResponseEntity<?> getMessages(HttpSession session,
                                         @RequestParam(required = false) Long lastMessageId) {

        try {
            User user = (User) session.getAttribute("user");
            if (user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            Long senderId = user.getId();
            Long receiverId = user.getId();
            Long groupId = user.getGroup().getId();

            List<Chat> messages;

            if(lastMessageId != null) {
                messages = chatRepository.findBySenderIdOrReceiverIdOrGroupIdAndIdGreaterThanOrderByTimeStampAsc(
                        senderId, receiverId, groupId, lastMessageId
                );
            } else {
                messages = chatRepository.findBySenderIdOrReceiverIdOrGroupIdOrderByTimeStampAsc(
                        senderId, receiverId, groupId
                );
            }

            return ResponseEntity.ok(messages);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }

}
