package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {



}
