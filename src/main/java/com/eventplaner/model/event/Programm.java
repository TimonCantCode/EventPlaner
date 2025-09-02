package com.eventplaner.model.event;

import com.eventplaner.model.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Programm {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
}
