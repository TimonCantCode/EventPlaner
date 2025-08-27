package com.eventplaner.controller.event;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.auth.UserRole;
import com.eventplaner.model.dto.EventForm;
import com.eventplaner.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventPageController {

    private UserProfileRepository userProfileRepository;
    private com.eventplaner.repository.EventRepository eventRepository;

    @Autowired
    public EventPageController(UserProfileRepository userProfileRepository,
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

    @GetMapping("/{userName}")
    public String userEvents(@PathVariable String userName,
                            Model model,
                            @AuthenticationPrincipal String loggedUserName) {
        UserProfile userProfile = this.userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<com.eventplaner.model.event.Event> events = eventRepository.findByAuthor_UserNameOrderByCreatedAtDesc(userName);

        model.addAttribute("user", userProfile);
        model.addAttribute("events", events);
        model.addAttribute("loggedUserName", loggedUserName);
        return "my-events";
    }

    @GetMapping("/all-events")
    public String showAllEvents(Model model,
                               @AuthenticationPrincipal String loggedUserName) {
        List<com.eventplaner.model.event.Event> eventList = this.eventRepository.findAll();
        UserProfile userProfile = this.userProfileRepository.findByUserName(loggedUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserRole loggedUserRole = userProfile.getRole();
        model.addAttribute("events", eventList);
        model.addAttribute("loggedUserName", loggedUserName);
        model.addAttribute("loggedUserRole", loggedUserRole.name());
        return "all-events";
    }

    @GetMapping("/{eventId}/edit")
    public String showEditEventForm(@PathVariable Long eventId,
                                   @AuthenticationPrincipal String userName,
                                   Model model) {

        UserProfile user = userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        com.eventplaner.model.event.Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!event.getAuthor().getUserName().equals(userName) && !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        model.addAttribute("event", event);
        return "edit_event";
    }

    @GetMapping("/details/{eventId}")
    public String showEventDetails(@PathVariable Long eventId, Model model) {
        com.eventplaner.model.event.Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("event", event);
        return "event_details";
    }

}
