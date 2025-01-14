package org.zelator.scheduler;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zelator.entity.MysteryChangeTask;
import org.zelator.repository.MysteryChangeTaskRepository;
import org.zelator.service.MysteryChangeService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MysteryChangeTaskScheduler {


    private final MysteryChangeTaskRepository mysteryChangeTaskRepository;
    private final MysteryChangeService mysteryChangeService;


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAndPerformMysteryChanges() {
        LocalDateTime now = LocalDateTime.now();
        List<MysteryChangeTask> tasks = mysteryChangeTaskRepository.findAllByStateAndEventDateBeforeWithMembers(
                MysteryChangeTask.State.PENDING, now);

        for(MysteryChangeTask task : tasks) {
            try{
                mysteryChangeService.performMysteryChange(task);
//                mysteryChangeTaskRepository.delete(task);
                // Usunięcie taskMembers przed usunięciem samego taska
                mysteryChangeTaskRepository.deleteTaskMembersByTaskId(task.getId());
                System.out.println("Usunięto zależne taskMembers.");

                // Usunięcie samego taska
                mysteryChangeTaskRepository.deleteTaskById(task.getId());
                System.out.println("Usunięto task.");
                System.out.println("Task wykonany i usuniety.");
            } catch (Exception e) {
                System.err.println("Błąd podczas wykonywania taska.");
                e.printStackTrace();
            }
        }
    }

}
