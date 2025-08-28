package com.eventplaner.controller.comment;

import com.eventplaner.model.dto.CommentForm;
import com.eventplaner.model.event.Event;
import com.eventplaner.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/comments")
public class CommentPageController {

    private EventRepository eventRepository;

    @Autowired
    public CommentPageController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/create/{eventId}")
    public String showCommentForm(@PathVariable Long eventId,
                                  @AuthenticationPrincipal String loggedUserName,
                                  @ModelAttribute CommentForm dto,
                                  Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("event", event);
        model.addAttribute("loggedUserName", loggedUserName);

        return "create_comment";
    }

}
