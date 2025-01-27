package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    // Wiadomości, w których użytkownik jest nadawcą lub odbiorcą, lub należy do grupy
    List<Chat> findBySenderIdOrReceiverIdOrGroupIdOrderByTimeStampAsc(Long senderId, Long receiverId, Long groupId);

    // Wiadomości tylko nowsze od podanego lastMessageId
    List<Chat> findBySenderIdOrReceiverIdOrGroupIdAndIdGreaterThanOrderByTimeStampAsc(
            Long senderId, Long receiverId, Long groupId, Long lastMessageId);

}
