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

    public UserProfile getProfile(String nameTag) {
        final var nameTagArr = nameTag.split("#");
        final var username = UserProfile.getUsername(nameTagArr);
        final long tag = UserProfile.parseTag(nameTagArr[1]);

        for (UserProfile userProfile : mockUserSource)
            if (tag == userProfile.getUserTag() && username.equals(userProfile.getUsername()))
                return userProfile;

        throw new NoSuchElementException(String.format("No Profile of name='%s' and tag=%d", username, tag));
    }

    public void postProfile(UserProfile userProfile) {
        if (mockUserSource.contains(userProfile))
            throw new ElementAlreadyExistException(userProfile.toString() + " already exists.");
        mockUserSource.add(userProfile);
    }

    public void deleteProfile(UserProfile userProfile) {
        if(mockUserSource.remove(userProfile)) return;
        throw new NoSuchElementException();
    }




}
