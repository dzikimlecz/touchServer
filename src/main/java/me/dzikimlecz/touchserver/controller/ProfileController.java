package me.dzikimlecz.touchserver.controller;


import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProfileController {

    private final List<UserProfile> mockUserSource = new ArrayList<>();
    public ProfileController() { mockUserSource.add(UserProfile.of("username", 1)); }

    public List<UserProfile> getProfiles() {
        return List.copyOf(mockUserSource);
    }

    public void postProfile(UserProfile userProfile) {
        if (mockUserSource.contains(userProfile))
            throw new ElementAlreadyExistException();
        mockUserSource.add(userProfile);
    }

    public void deleteProfile(UserProfile userProfile) {
        if(mockUserSource.remove(userProfile)) return;
        throw new NoSuchElementException();
    }


}
