package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.PrayerStatus;
import org.zelator.entity.User;
import org.zelator.repository.PrayerStatusRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrayerStatusService {


    private final PrayerStatusRepository prayerStatusRepository;
    private final CalendarEventService calendarEventService;


    public void handleDailyPrayerStatus(User user) {
        LocalDate today = LocalDate.now();

        Optional<PrayerStatus> optionalPrayerStatus = prayerStatusRepository.findByUser(user);

        if(optionalPrayerStatus.isPresent()){
            PrayerStatus prayerStatus = optionalPrayerStatus.get();

            if(!prayerStatus.getPrayerDate().isEqual(today)) {
                if(!prayerStatus.getStatus()) {
                    calendarEventService.createMissedPrayerEvent(user, prayerStatus.getPrayerDate());
                }
                prayerStatus.setPrayerDate(today);
                prayerStatus.setStatus(false);
                prayerStatusRepository.save(prayerStatus);
            }
        }
    }


    public PrayerStatus createNewPrayerStatus(User user, LocalDate today) {
        PrayerStatus prayerStatus = new PrayerStatus();
        prayerStatus.setUser(user);
        prayerStatus.setPrayerDate(today);
        prayerStatus.setStatus(false);
        return prayerStatusRepository.save(prayerStatus);
    }


    public void markPrayerAsComleted(User user) {
        PrayerStatus prayerStatus = prayerStatusRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono dzisiejszej modlitwy."));

        prayerStatus.setStatus(true);
        prayerStatusRepository.save(prayerStatus);
    }


    public PrayerStatus getByUserId(Long userId) {
        return prayerStatusRepository.findByUserId(userId).orElse(null);
    }

}
