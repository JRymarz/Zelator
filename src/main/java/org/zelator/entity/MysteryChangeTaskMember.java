package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mystery_change_task_member")
@Getter
@Setter
public class MysteryChangeTaskMember {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mystery_change_task_id", nullable = false)
    private MysteryChangeTask mysteryChangeTask;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mystery_id", nullable = false)
    private Mystery mystery;

}
