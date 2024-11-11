package org.zelator.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mass_request")
@Getter
@Setter
public class MassRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String intention;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false)
    private LocalDateTime massDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MassStatus status;


    public enum MassStatus{
        PENDING,
        APPROVED,
        REJECTED
    }

}
