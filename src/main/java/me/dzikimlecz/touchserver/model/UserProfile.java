package me.dzikimlecz.touchserver.model;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class UserProfile {
    public static UserProfile of(@NotNull String username, long userTag) {
        if (username.isEmpty())
            throw new IllegalArgumentException("Username can't be empty.");
        if (userTag == 0)
            throw new IllegalArgumentException("User's tag can't be set to 0.");
        return new UserProfile(username, userTag);
    }

    public static final UserProfile NULL_USER = new UserProfile("", 0);

    public String getUsername() {
        return username;
    }
    private final String username;

    public long getUserTag() {
        return userTag;
    }
    private final long userTag;

    private UserProfile(@NotNull String username, long userTag) {
        this.username = username;
        this.userTag = userTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() != UserProfile.class) return false;
        UserProfile that = (UserProfile) o;
        return userTag == that.userTag && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userTag);
    }

    @Override
    public String toString() {
        return String.format("UserProfile{username='%s', userTag=%d}", username, userTag);
    }
}
