package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zelator.entity.MysteryChangeTask;

import java.time.LocalDateTime;
import java.util.List;

public interface MysteryChangeTaskRepository extends JpaRepository<MysteryChangeTask, Long> {


    List<MysteryChangeTask> findAllByStateAndEventDateBefore(MysteryChangeTask.State state, LocalDateTime eventDate);


    @Query("SELECT t FROM MysteryChangeTask t JOIN FETCH t.taskMembers WHERE t.state = :state AND t.eventDate <= :currentDate")
    List<MysteryChangeTask> findAllByStateAndEventDateBeforeWithMembers(
            @Param("state") MysteryChangeTask.State state,
            @Param("currentDate") LocalDateTime currentDate
    );


    // Zapytanie usuwające powiązane taskMembers
    @Modifying
    @Query("DELETE FROM MysteryChangeTaskMember m WHERE m.mysteryChangeTask.id = :taskId")
    void deleteTaskMembersByTaskId(@Param("taskId") Long taskId);

    // Zapytanie usuwające sam task
    @Modifying
    @Query("DELETE FROM MysteryChangeTask t WHERE t.id = :taskId")
    void deleteTaskById(@Param("taskId") Long taskId);

}
