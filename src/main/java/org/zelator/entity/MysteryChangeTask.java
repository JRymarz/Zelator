package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mystery_change_task")
@Getter
@Setter
public class MysteryChangeTask {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "intention_id", nullable = false)
    private Intention intention;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private State state;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @OneToMany(mappedBy = "mysteryChangeTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MysteryChangeTaskMember> taskMembers;

    @OneToOne
    @JoinColumn(name = "calendar_event_id")
    private CalendarEvent calendarEvent;


    public enum State {
        PENDING,
        EXECUTED,
        CANCELLED
    }

}
