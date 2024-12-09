package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zelator.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<List<User>> findByGroupId(Long groupId);

}
