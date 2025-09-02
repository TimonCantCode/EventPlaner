package com.eventplaner.controller.programm;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.auth.UserRole;
import com.eventplaner.model.dto.ProgrammForm;
import com.eventplaner.model.event.Event;
import com.eventplaner.model.event.Programm;
import com.eventplaner.repository.EventRepository;
import com.eventplaner.repository.UserProfileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/programms")
public class ProgrammController {

    private UserProfileRepository  userProfileRepository;
    private EventRepository eventRepository;

    @Autowired
    public ProgrammController(UserProfileRepository userProfileRepository, EventRepository eventRepository) {
        this.userProfileRepository = userProfileRepository;
        this.eventRepository = eventRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create/{eventId}")
    public String createProgramm(@AuthenticationPrincipal String loggedUserName,
                                @PathVariable Long eventId,
                                @Valid @ModelAttribute("programmForm") ProgrammForm dto){

        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserProfile author = this.userProfileRepository.findByUserName(loggedUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UserProfile user = userProfileRepository.findByUserName(loggedUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Programm programm = new Programm();
        programm.setContent(dto.content());
        programm.setTitle(dto.title());
        programm.setEvent(event);
        programm.setAuthor(author);

        event.getProgramms().add(programm);
        this.eventRepository.save(event);

        return "redirect:/events/details/" + eventId;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{eventId}/{programmId}")
    public String deleteProgramm(@PathVariable Long eventId,
                             @PathVariable Long programmId,
                             @AuthenticationPrincipal String loggedUserName) {

        UserProfile user = userProfileRepository.findByUserName(loggedUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Programm programm = event.getProgramms().stream()
                .filter(c -> programmId.equals(c.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        event.getProgramms().remove(programm);

        this.eventRepository.save(event);

        return "redirect:/events/details/" + eventId;

    }
}
