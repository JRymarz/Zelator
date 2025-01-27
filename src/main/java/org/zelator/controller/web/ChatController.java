package org.zelator.controller.web;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.ChatDto;
import org.zelator.dto.MessageRequest;
import org.zelator.entity.Chat;
import org.zelator.entity.Group;
import org.zelator.entity.User;
import org.zelator.repository.ChatRepository;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {


    private final ChatRepository chatRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


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


    @GetMapping("/chat/conversations")
    @CrossOrigin
    public ResponseEntity<?> getConversations(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or group not found");

            Group group = user.getGroup();
            List<User> groupMembers = groupRepository.findMembersByGroupId(group.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("group", Map.of(
                    "id", group.getId(),
                    "name", group.getName()
            ));

            response.put("members", groupMembers.stream()
                    .filter(member -> !member.getId().equals(user.getId()))
                    .map(member -> Map.of(
                            "id", member.getId(),
                            "firstName", member.getFirstName(),
                            "lastName", member.getLastName()
                    ))
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @GetMapping("/chat/conversation")
    @CrossOrigin
    public ResponseEntity<?> getConversationMessages(HttpSession session,
                                                     @RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) Long groupId) {

        try {
            User user = (User) session.getAttribute("user");
            if(user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            Long currentUserId = user.getId();

            List<Chat> messages;

            if(groupId != null){
                messages = chatRepository.findByGroupIdOrderByTimeStampAsc(groupId);
            } else if(userId != null) {
                messages = chatRepository.findConversationBetweenUsers(currentUserId, userId);
            } else {
                return ResponseEntity.badRequest().body("userId or groupId must be provided");
            }

            List<ChatDto> response = messages.stream()
                    .map(chat -> new ChatDto(
                            chat.getId(),
                            chat.getMessage(),
                            chat.getTimeStamp(),
                            chat.getIsRead(),
                            chat.getSender() != null ? chat.getSender().getFirstName() + " " + chat.getSender().getLastName() : null,
                            chat.getGroup() != null ? chat.getGroup().getName() : null
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @PostMapping("/chat/send")
    @CrossOrigin
    public ResponseEntity<?> sendMessage(HttpSession session,
                                         @RequestBody MessageRequest request) {

        try {
            User sender = (User) session.getAttribute("user");
            if(sender == null || sender.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            Chat chat = new Chat();
            chat.setSender(sender);
            chat.setMessage(request.getMessage());

            if(request.getGroupId() != null) {
                Group group = sender.getGroup();
                chat.setGroup(group);
            } else if (request.getReceiverId() != null) {
                User receiver = userRepository.findById(request.getReceiverId())
                        .orElseThrow(null);
                if (receiver == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found");

                chat.setReceiver(receiver);
            } else {
                return ResponseEntity.badRequest().body("ReceiverId or GroupId must be provided");
            }

            chatRepository.save(chat);
            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @PatchMapping("/chat/mark-as-read")
    @CrossOrigin
    public ResponseEntity<?> markMessagesAsRead(
            @RequestParam(required = false) Long receiverId,
            @RequestParam(required = false) Long groupId,
            HttpSession session) {

        try {
            User currentUser = (User) session.getAttribute("user");
            if(currentUser == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

            if(receiverId == null && groupId == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie ma wiadomosci do oznaczenia");

            List<Chat> messages;
            if(groupId != null){
                messages = chatRepository.findUnreadGroupMessages(groupId);
            } else {
                messages = chatRepository.findUnreadPrivateMessages(currentUser.getId(), receiverId);
            }

            for (Chat message : messages) {
                message.setIsRead(true);
            }

            chatRepository.saveAll(messages);

            return ResponseEntity.ok("Messages marked as read");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }

}
