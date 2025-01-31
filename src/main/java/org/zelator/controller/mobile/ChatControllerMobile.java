package org.zelator.controller.mobile;


import jakarta.transaction.Transactional;
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
import org.zelator.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ChatControllerMobile {


    private final UserService userService;
    private final GroupRepository groupRepository;
    private final ChatRepository chatRepository;


    @GetMapping("/mob/chat/conversations")
    @CrossOrigin
    public ResponseEntity<?> getConversations(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

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


    @GetMapping("/mob/chat/conversation")
    @CrossOrigin
    public ResponseEntity<?> getConversationMessages(@RequestHeader("User-ID") Long currentUserId,
                                                     @RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) Long groupId) {

        try {
            User currentUser = userService.getById(currentUserId);
            if(currentUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            List<Chat> messages;

            if(groupId != null){
                messages = chatRepository.findByGroupIdOrderByTimeStampAsc(groupId);
            } else if (userId != null) {
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
                            chat.getGroup() != null ? chat.getGroup().getName() : null,
                            chat.getSender().getId()
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @PostMapping("/mob/chat/send")
    @CrossOrigin
    public ResponseEntity<?> sendMessage(@RequestHeader("User-ID") Long userId,
                                         @RequestBody MessageRequest request) {

        try {
            User sender = userService.getById(userId);
            if(sender == null || sender.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            Chat chat = new Chat();
            chat.setSender(sender);
            chat.setMessage(request.getMessage());

            if (request.getGroupId() != null) {
                Group group = sender.getGroup();
                chat.setGroup(group);
            } else if (request.getReceiverId() != null) {
                User receiver = userService.getById(request.getReceiverId());
                if(receiver == null)
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found");

                chat.setReceiver(receiver);
            } else {
                return ResponseEntity.badRequest().body("ReceiverId or GroupId must be provided");
            }

            chatRepository.save(chat);

            ChatDto response = new ChatDto(
                    chat.getId(),
                    chat.getMessage(),
                    chat.getTimeStamp(),
                    chat.getIsRead(),
                    chat.getSender() != null ? chat.getSender().getFirstName() + " " + chat.getSender().getLastName() : null,
                    chat.getGroup() != null ? chat.getGroup().getName() : null,
                    chat.getSender().getId()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @PatchMapping("/mob/chat/mark-as-read")
    @CrossOrigin
    public ResponseEntity<?> markMessageAsRead(@RequestHeader("User-ID") Long userId,
                                               @RequestParam(required = false) Long receiverId,
                                               @RequestParam(required = false) Long groupId) {

        try {
            User currentUser = userService.getById(userId);
            if (currentUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            if(receiverId == null && groupId == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie ma wiadomości do oznaczenia");

            List<Chat> messages;
            if(groupId != null) {
                messages = chatRepository.findUnreadGroupMessages(groupId);
            } else {
                messages = chatRepository.findUnreadPrivateMessages(currentUser.getId(), receiverId);
            }

            for(Chat message : messages) {
                message.setIsRead(true);
            }

            chatRepository.saveAll(messages);
            return ResponseEntity.ok("Messages marked as read");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }


    @GetMapping("/mob/chat/unread-conversations")
    @CrossOrigin
    public ResponseEntity<?> getUnreadConversations(@RequestHeader("User-ID") Long userId) {
        try {
            User currentUser = userService.getById(userId);
            if (currentUser == null || currentUser.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

//            Long userId = currentUser.getId();
            Long groupId = currentUser.getGroup().getId();

            List<Long> unreadUserMessages = chatRepository.findUnreadUserMessages(userId);
            Boolean unreadGroupMessages = chatRepository.hasUnreadGroupMessages(groupId);

            Map<String, Object> response = new HashMap<>();
            response.put("unreadUserConversations", unreadUserMessages);
            response.put("unreadGroupConversation", unreadGroupMessages ? groupId : null);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @GetMapping("/mob/chat/are-unread")
    @CrossOrigin
    public ResponseEntity<?> areUnread(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if (user == null || user.getGroup() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

//            Long userId = user.getId();
            Long groupId = user.getGroup().getId();

            List<Long> unreadUserMessages = chatRepository.findUnreadUserMessages(userId);
            Boolean unreadGroupMessages = chatRepository.hasUnreadGroupMessages(groupId);

            Boolean response = false;
            if(unreadGroupMessages || !unreadUserMessages.isEmpty())
                response = true;

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd");
        }
    }


    @GetMapping("/mob/chat/notifications")
    @CrossOrigin
    public ResponseEntity<?> getMyNotifiactions(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Use nto found");

            Long currentUserId = user.getId();
            List<Chat> notifications = chatRepository.findByReceiverIdAndSenderIsNullOrderByTimeStampAsc(currentUserId);

            List<ChatDto> response = notifications.stream()
                    .map(chat -> new ChatDto(
                            chat.getId(),
                            chat.getMessage(),
                            chat.getTimeStamp(),
                            chat.getIsRead(),
                            null,
                            null,
                            null
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }


    @PostMapping("/mob/chat/read-notifications")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> markNotificationsAsRead(@RequestHeader("User-ID") Long userId) {
        try {
            User user = userService.getById(userId);
            if(user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            Long currentUserId = user.getId();
            List<Chat> notifications = chatRepository.findByReceiverIdAndSenderIsNullOrderByTimeStampAsc(currentUserId);

            for(Chat noti : notifications) {
                noti.setIsRead(true);
            }

            chatRepository.saveAll(notifications);

            return ResponseEntity.ok("Marked as read");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

}
