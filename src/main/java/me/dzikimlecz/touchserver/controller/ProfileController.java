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
        final var username = getUsername(nameTagArr);
        final long tag = parseTag(nameTagArr);

        for (UserProfile userProfile : mockUserSource)
            if (tag == userProfile.getUserTag() && username.equals(userProfile.getUsername()))
                return userProfile;

        throw new NoSuchElementException(String.format("No Profile of name='%s' and tag=%d", username, tag));
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

    private String getUsername(String[] nameTagArr) {
        switch (nameTagArr.length) {
            case 0:
                throw new IllegalArgumentException("NameTag can't be empty.");
            case 1:
                throw new IllegalArgumentException("'" + nameTagArr[0] + "' is not a valid NameTag.");
            case 2:
                return nameTagArr[0];
            default:
                throw new IllegalArgumentException(String.join("#", nameTagArr) + " is not a valid NameTag.");
        }
    }

    private long parseTag(String[] nameTagArr) {
        final long tag;
        try {
            tag = Long.parseLong(nameTagArr[1]);
            if (tag == 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + nameTagArr[1] + "' is not a valid tag.");
        }
        return tag;
    }


}
