package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_event")
@Getter
@Setter
public class CalendarEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private State state;

    @OneToOne(mappedBy = "calendarEvent")
    private MysteryChangeTask mysteryChangeTask;


    public enum EventType{
        MYSTERYCHANGE,
        MASS,
        PRAYER,
        OTHER
    }

    public enum State{
        scheduled,
        completed,
        undone
    }
}
