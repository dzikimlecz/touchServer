package me.dzikimlecz.touchserver.model.database.entities;


import me.dzikimlecz.touchserver.model.UserProfile;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {
    @GeneratedValue(strategy= GenerationType.AUTO)
    private @Id Integer id;
    private String username;
    private long userTag;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserTag() {
        return userTag;
    }

    public void setUserTag(long userTag) {
        this.userTag = userTag;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public UserEntity() {}

    public UserEntity(@NotNull UserProfile profile) {
        setUsername(profile.getUsername());
        setUserTag(profile.getUserTag());
    }

    public UserProfile asProfile() {
        return UserProfile.of(username, userTag);
    }

    public static UserEntity create(UserProfile profile) {
        final var entity = new UserEntity();
        entity.username = profile.getUsername();
        entity.userTag = profile.getUserTag();
        return entity;
    }
}
