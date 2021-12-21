package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.container.Container;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface UserProfileService {
    Collection<UserProfile> getProfiles();

    UserProfile findById(Integer id);

    UserProfile findByNameTag(@NotNull String nameTag);

    void add(UserProfile profile);

    UserProfile delete(UserProfile profile);

    Integer findId(UserProfile profile);

    void reset();

    Container<UserProfile> getProfilesPage(int page, int size);
}
