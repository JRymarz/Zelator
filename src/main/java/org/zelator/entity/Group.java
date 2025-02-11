package org.zelator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rosary_group")
@Getter
@Setter
public class Group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = true)
    private User leader;

    @ManyToOne
    @JoinColumn(name = "intention_id", nullable = false)
    private Intention intention;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<User> members = new ArrayList<>();

}
