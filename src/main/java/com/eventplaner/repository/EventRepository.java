package com.eventplaner.repository;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.event.Event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByAuthor_UserNameOrderByCreatedAtDesc(String userName);
    List<Event> findByParticipantsContaining(UserProfile userProfile);
}
