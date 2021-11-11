package me.dzikimlecz.touchserver.model.database.entities;


import me.dzikimlecz.touchserver.model.UserProfile;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEntity {
    private Long id;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public UserEntity() {}

    public UserEntity(@NotNull UserProfile profile) {
        setUsername(profile.getUsername());
        setUserTag(profile.getUserTag());
    }
}
