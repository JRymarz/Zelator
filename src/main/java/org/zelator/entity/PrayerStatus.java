package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prayer_status")
@Getter
@Setter
public class PrayerStatus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mystery_id", nullable = false)
    private Mystery mystery;

    @Column(nullable = false)
    private Boolean status = false;

    @Column(nullable = false)
    private LocalDateTime prayerDate;

}
