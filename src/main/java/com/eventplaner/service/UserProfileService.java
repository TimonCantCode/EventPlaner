package com.dblayer.service;

import com.dblayer.model.UserProfile;
import com.dblayer.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    // dependency injection
    // wenn userprofileservice initialisiert wird, wird ein neues objekt
    // der klasse userprofilerepository erzeugt. das passiert vollautomatisch
    // weil spring weiss, dass beide beans sind.
    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUser(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public void deleteUserById(Long id) {
        userProfileRepository.deleteById(id);
    }

    public List<UserProfile> getAllUser() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public Optional<UserProfile> getUserProfileByUsername(String username) { return this.userProfileRepository.findByUserName(username); }
}
