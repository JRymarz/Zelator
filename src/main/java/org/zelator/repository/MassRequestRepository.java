package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zelator.entity.MassRequest;
import org.zelator.entity.User;

import java.util.List;
import java.util.Optional;

public interface MassRequestRepository extends JpaRepository<MassRequest, Long> {


    Optional<List<MassRequest>> findByUser(User user);

    List<MassRequest> findByUserIdIn(List<Long> userIds);

}
