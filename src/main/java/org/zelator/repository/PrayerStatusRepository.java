package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.PrayerStatus;
import org.zelator.entity.User;

import java.util.Optional;

public interface PrayerStatusRepository extends JpaRepository<PrayerStatus, Long> {


    Optional<PrayerStatus> findByUser(User user);

    Optional<PrayerStatus> findByUserId(Long userId);

}
