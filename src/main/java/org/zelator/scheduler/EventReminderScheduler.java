package org.zelator.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.Chat;
import org.zelator.entity.PrayerStatus;
import org.zelator.entity.User;
import org.zelator.repository.CalendarEventRepository;
import org.zelator.repository.ChatRepository;
import org.zelator.repository.PrayerStatusRepository;
import org.zelator.repository.UserRepository;
import org.zelator.service.UserService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventReminderScheduler {


    private final ChatRepository chatRepository;
    private final CalendarEventRepository eventRepository;
    private final UserRepository userRepository;
    private final PrayerStatusRepository prayerStatusRepository;
    private final UserService userService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void sendEventReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<CalendarEvent> events = eventRepository.findByEventDate(tomorrow);

        for(CalendarEvent event : events) {
            List<User> receipents = new ArrayList<>();

            if(event.getGroup() != null) {
                receipents.addAll(userRepository.findByGroupId(event.getGroup().getId()).orElse(null));
            } else if (event.getCreator() != null) {
                receipents.add(event.getCreator());
            }

            for(User user : receipents) {
                Chat reminder = new Chat();
                reminder.setReceiver(user);
                reminder.setMessage("Przypomnienie: Jutro odbędzie się wydarzenie: " + event.getTitle());
                reminder.setIsRead(false);
                reminder.setTimeStamp(LocalDateTime.now());

                chatRepository.save(reminder);
            }
        }
    }


    @Scheduled(fixedRate = 30000)
    public void sendPrayerReminders() {
        System.out.println("JEstem w metodzie.");
        LocalTime currentTime = LocalTime.now();

        List<PrayerStatus> prayerStatuses = prayerStatusRepository.findAll();

        for(PrayerStatus prayerStatus : prayerStatuses) {
            LocalTime reminderTime = prayerStatus.getPrayerReminderTime();
            System.out.println("Sprawdzilem date" + reminderTime);

            if(reminderTime != null && isTimeCloseEnough(reminderTime, currentTime)) {
                User user = userService.getById(prayerStatus.getUser().getId());
                System.out.println("Mam usera");
                if(user != null) {
                    Chat reminder = new Chat();
                    reminder.setReceiver(user);
                    reminder.setMessage("Przypomnienie: Czas na modlitwę!");
                    reminder.setIsRead(false);
                    reminder.setTimeStamp(LocalDateTime.now());
                    System.out.println("Ustawilem remindera");

                    chatRepository.save(reminder);
                }
            }
        }
    }

    public boolean isTimeCloseEnough(LocalTime reminderTime, LocalTime currentTime) {
        if (reminderTime == null || currentTime == null) {
            return false;
        }

        // Oblicz różnicę między dwoma czasami
        Duration duration = Duration.between(reminderTime, currentTime);

        // Tolerancja 1 minuta (60 sekund), można to dostosować do swoich potrzeb
        long toleranceInSeconds = 60;

        // Jeśli różnica w sekundach jest mniejsza niż tolerancja, uznajemy je za równe
        return Math.abs(duration.getSeconds()) <= toleranceInSeconds;
    }

}
