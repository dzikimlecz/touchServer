package me.dzikimlecz.touchserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class UserProfile {
    private final String username;
    public String getUsername() {
        return username;
    }

    private final long userTag;
    public long getUserTag() {
        return userTag;
    }

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
    @Contract(pure = true)
    public String toString() {
        return String.format("UserProfile{username='%s', userTag=%d}", username, userTag);
    }

    @Contract(pure = true)
    @JsonIgnore
    public @NotNull String getNameTag() {
        return username + '#' + userTag;
    }


    @Contract("_, _ -> new")
    public static @NotNull UserProfile of(@NotNull String username, long userTag) {
        if (username.isEmpty())
            throw new IllegalArgumentException("Username can't be empty.");
        if (userTag == 0)
            throw new IllegalArgumentException("User's tag can't be set to 0.");
        return new UserProfile(username, userTag);
    }

    public static String getUsername(String @NotNull [] nameTagArr) {
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

    public static long parseTag(@NotNull String tagString) {
        final long tag;
        try {
            tag = Long.parseLong(tagString);
            if (tag == 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + tagString + "' is not a valid tag.");
        }
        return tag;
    }

    public static final UserProfile NULL_USER = new UserProfile("", 0);

}
