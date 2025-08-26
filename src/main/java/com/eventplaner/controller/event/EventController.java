package com.eventplaner.controller.event;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.dto.EventForm;
import com.eventplaner.repository.UserProfileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/events")
public class EventController {

    private UserProfileRepository userProfileRepository;
    private com.eventplaner.repository.EventRepository eventRepository;

    @Autowired
    public EventController(UserProfileRepository userProfileRepository,
                           com.eventplaner.repository.EventRepository eventRepository) {
        this.userProfileRepository = userProfileRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/new")
    public String showCreateEventForm(@ModelAttribute EventForm dto,
                                     @AuthenticationPrincipal String userName,
                                     Model model) {
        model.addAttribute("authorUserName", userName);
        return "create_event";
    }

    @PostMapping
    public String createEvent(@Valid @ModelAttribute("eventForm") EventForm dto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal String userName,
                             Model model){

        if (bindingResult.hasErrors()) {
            return "create_event";
        }

        UserProfile user = this.userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        com.eventplaner.model.event.Event event = new com.eventplaner.model.event.Event();
        event.setContent(dto.content());
        event.setTitle(dto.title());
        event.setCreatedAt(LocalDateTime.now());
        event.setAuthor(user);

        this.eventRepository.save(event);

        return "redirect:/events/" + userName;
    }

    @PostMapping("/{eventId}/delete")
    public String deleteEvent(@PathVariable Long eventId,
                             @AuthenticationPrincipal String userName) {

        UserProfile user = userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        com.eventplaner.model.event.Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!event.getAuthor().getUserName().equals(userName) && !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        eventRepository.delete(event);
        return "redirect:/events/" + userName;
    }

    @PostMapping("/{eventId}/edit")
    public String updateEvent(@PathVariable Long eventId,
                             @Valid @ModelAttribute EventForm dto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal String userName) {
        if (bindingResult.hasErrors()) {
            return "edit_event";
        }
        UserProfile user = userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        com.eventplaner.model.event.Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!event.getAuthor().getUserName().equals(userName) && !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        event.setTitle(dto.title());
        event.setContent(dto.content());
        eventRepository.save(event);

        return "redirect:/events/" + userName;
    }
}
