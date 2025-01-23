package org.zelator.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zelator.entity.PrayerStatus;
import org.zelator.entity.User;
import org.zelator.service.PrayerStatusService;
import org.zelator.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PrayerStatusScheduler {


    private final PrayerStatusService prayerStatusService;
    private final UserService userService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePrayerStatuses() {
        List<User> users = userService.getAllUsers();

        users.forEach(user -> prayerStatusService.handleDailyPrayerStatus(user));
    }

}
