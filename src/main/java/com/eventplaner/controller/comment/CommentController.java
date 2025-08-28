package com.eventplaner.controller.comment;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.auth.UserRole;
import com.eventplaner.model.dto.CommentForm;
import com.eventplaner.model.event.Comment;
import com.eventplaner.model.event.Event;
import com.eventplaner.repository.EventRepository;
import com.eventplaner.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private UserProfileRepository  userProfileRepository;
    private EventRepository eventRepository;

    @Autowired
    public CommentController(UserProfileRepository userProfileRepository, EventRepository eventRepository) {
        this.userProfileRepository = userProfileRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/create/{eventId}")
    public String createComment(@AuthenticationPrincipal String loggedUserName,
                                @PathVariable Long eventId,
                                @Valid @ModelAttribute("commentForm") CommentForm dto){

        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserProfile author = this.userProfileRepository.findByUserName(loggedUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setTitle(dto.title());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        event.getComments().add(comment);
        this.eventRepository.save(event);

        return "redirect:/events/details/" + eventId;

    }
    
    @PostMapping("/delete/{eventId}/{commentId}")
    public String deleteComment(@PathVariable Long eventId,
                             @PathVariable Long commentId,
                             @AuthenticationPrincipal String loggeduserName) {

        UserProfile user = userProfileRepository.findByUserName(loggeduserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment comment = event.getComments().stream()
                .filter(c -> commentId.equals(c.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        event.getComments().remove(comment);

        this.eventRepository.save(event);

        return "redirect:/events/details/" + eventId;

    }
}
