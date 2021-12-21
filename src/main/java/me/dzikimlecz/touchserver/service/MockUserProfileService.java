package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.container.Container;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class MockUserProfileService implements UserProfileService {

    private final List<UserProfile> profiles = new ArrayList<>();

    @Override
    public Collection<UserProfile> getProfiles() {
        return List.copyOf(profiles);
    }

    @Override
    // not used in controller
    public UserProfile findById(Integer id) {
        return null;
    }

    @Override
    public UserProfile findByNameTag(@NotNull String nameTag) {
        return profiles.stream()
                .filter(profile -> nameTag.equals(profile.getNameTag()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void add(UserProfile profile) {
        if (profiles.contains(profile))
            throw new ElementAlreadyExistException();
        profiles.add(profile);
    }

    @Override
    public UserProfile delete(UserProfile profile) {
        if (profiles.remove(profile))
            return profile;
        throw new NoSuchElementException();
    }

    @Override
    public Integer findId(UserProfile profile) {
        return null;
    }

    @Override
    public void reset() {
        profiles.clear();
    }

    @Override
    public Container<UserProfile> getProfilesPage(int page, int size) {
        return null;
    }
}
