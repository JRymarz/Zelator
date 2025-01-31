package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "prayer_status")
@Getter
@Setter
public class PrayerStatus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean status = false;

    @Column(nullable = false)
    private LocalDate prayerDate;

    @Column(name = "prayer_reminder_time", nullable = true)
    private LocalTime prayerReminderTime;

}
