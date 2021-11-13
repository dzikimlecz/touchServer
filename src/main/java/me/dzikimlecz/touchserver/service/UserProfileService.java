package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.UserProfile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface UserProfileService {
    Collection<UserProfile> getProfiles();

    UserProfile findById(Integer id);

    UserProfile findByNameTag(@NotNull String nameTag);

    void add(UserProfile profile);

    UserProfile delete(UserProfile profile);

    Integer findId(UserProfile profile);

    void reset();
}
