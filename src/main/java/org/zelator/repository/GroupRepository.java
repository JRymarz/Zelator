package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zelator.entity.Group;
import org.zelator.entity.User;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {


    @Query("SELECT g.members FROM Group g WHERE g.id = :groupId")
    List<User> findMembersByGroupId(@Param("groupId") Long groupId);

}
