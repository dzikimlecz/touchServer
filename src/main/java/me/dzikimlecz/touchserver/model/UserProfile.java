package me.dzikimlecz.touchserver.model;

public final class UserProfile {
    private final String username;
    private final long userTag;

    public UserProfile(String username, long userTag) {
        this.username = username;
        this.userTag = userTag;
    }
}
