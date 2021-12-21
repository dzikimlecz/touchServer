package me.dzikimlecz.touchserver.model.container;

import me.dzikimlecz.touchserver.model.UserProfile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public final class UserProfileContainer extends Container<UserProfile> {
    private final List<UserProfile> elements;

    public UserProfileContainer(int index, int numberOfPages, List<UserProfile> elements) {
        super(index, numberOfPages);
        this.elements = elements;
    }

    @Override
    public List<UserProfile> getElements() {
        return elements;
    }

    @Contract("_ -> new")
    public static @NotNull UserProfileContainer wrapPage(@NotNull Page<UserProfile> page) {
        return new UserProfileContainer(page.getNumber(), page.getTotalPages(), page.getContent());
    }
}
