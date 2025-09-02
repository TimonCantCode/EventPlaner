package com.eventplaner.controller.programm;

import com.eventplaner.model.dto.ProgrammForm;
import com.eventplaner.model.event.Event;
import com.eventplaner.repository.EventRepository;
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

@Controller
@RequestMapping("/programms")
public class ProgrammPageController {

    private EventRepository eventRepository;

    @Autowired
    public ProgrammPageController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/create/{eventId}")
    public String showProgrammForm(@PathVariable Long eventId,
                                  @AuthenticationPrincipal String loggedUserName,
                                  @ModelAttribute ProgrammForm dto,
                                  Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("event", event);
        model.addAttribute("loggedUserName", loggedUserName);

        return "create_programm";
    }

}
