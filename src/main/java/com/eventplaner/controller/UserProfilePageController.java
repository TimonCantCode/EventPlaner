package com.dblayer.controller;

import com.dblayer.model.UserProfile;
import com.dblayer.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UserProfilePageController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfilePageController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profile")
    public String myProfile(@AuthenticationPrincipal String userName,
                            Model model) {
       Optional<UserProfile> user = this.userProfileService.getUserProfileByUsername(userName);
       if (user.isPresent()) {
           model.addAttribute("user", user.get());
           return "profile";
       } else {
           return "user-not-found";
       }
    }

    @GetMapping("/all-users")
    public String allUsers(Model model) {
        List<UserProfile> userProfileList = this.userProfileService.getAllUser();
        model.addAttribute("users", userProfileList);
        return "all-users";
    }

}
