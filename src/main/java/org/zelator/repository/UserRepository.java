package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {



}
