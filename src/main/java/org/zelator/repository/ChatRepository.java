package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zelator.entity.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    // Wiadomości, w których użytkownik jest nadawcą lub odbiorcą, lub należy do grupy
    List<Chat> findBySenderIdOrReceiverIdOrGroupIdOrderByTimeStampAsc(Long senderId, Long receiverId, Long groupId);

    // Wiadomości tylko nowsze od podanego lastMessageId
    List<Chat> findBySenderIdOrReceiverIdOrGroupIdAndIdGreaterThanOrderByTimeStampAsc(
            Long senderId, Long receiverId, Long groupId, Long lastMessageId);


    List<Chat> findByGroupIdOrderByTimeStampAsc(Long groupId);

    List<Chat> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimeStampAsc(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2);

    @Query("SELECT c FROM Chat c WHERE " +
            "(c.sender.id = :currentUserId AND c.receiver.id = :userId) " +
            "OR (c.sender.id = :userId AND c.receiver.id = :currentUserId) " +
            "ORDER BY c.timeStamp ASC")
    List<Chat> findConversationBetweenUsers(@Param("currentUserId") Long currentUserId,
                                            @Param("userId") Long userId);


    @Query("SELECT c FROM Chat c WHERE c.group.id = :groupId AND c.isRead = false")
    List<Chat> findUnreadGroupMessages(@Param("groupId") Long groupId);

    @Query("SELECT c FROM Chat c WHERE c.receiver.id = :receiverId AND c.sender.id = :senderId AND c.isRead = false")
    List<Chat> findUnreadPrivateMessages(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);

}
