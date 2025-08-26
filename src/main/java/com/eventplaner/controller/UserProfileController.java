package com.eventplaner.controller;

import com.eventplaner.model.UserProfile;
import com.eventplaner.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public UserProfile createUser(@RequestBody UserProfile userProfile) {
        return this.userProfileService.createUser(userProfile);
    }

    @GetMapping("/show")
    public Optional<UserProfile> showUserProfile(@RequestParam("id") Long id) {
        return this.userProfileService.getUserProfileById(id);
    }

    @DeleteMapping("/delete")
    public void deleteUserProfilebyID(@RequestParam("id") Long id) {
        this.userProfileService.deleteUserById(id);
    }

    @GetMapping("/all")
    public List<UserProfile> getAllUserProfiles() {
        return this.userProfileService.getAllUser();
    }

}
