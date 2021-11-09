package me.dzikimlecz.touchserver.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    @DisplayName("Should create user with given arguments")
    void creatingTest() {
        // when
        final var username = "UserName";
        final var tag = 100;
        // then
        assertDoesNotThrow(() -> UserProfile.of(username, tag));
    }

    @Test
    @DisplayName("Should fail to create user with empty name")
    void creatingEmptyNameTest() {
        // when
        final var username = "";
        final var tag = 100;
        // then
        assertThrows(IllegalArgumentException.class, () -> UserProfile.of(username, tag));
    }

    @Test
    @DisplayName("Should fail to create user with null name")
    void creatingNullNameTest() {
        // when
        final String username = null;
        final var tag = 100;
        // then
        assertThrows(NullPointerException.class, () -> UserProfile.of(username, tag));
    }

    @Test
    @DisplayName("Should fail to create user with illegal tag")
    void creatingIllegalTagTest() {
        // when
        final var username = "UserName";
        final var tag = 0;
        // then
        assertThrows(IllegalArgumentException.class, () -> UserProfile.of(username, tag));
    }

    @Test
    @DisplayName("Should consider Profiles of equal fields equal")
    void equalsTest() {
        // given
        final var username = "UserName";
        final var tag = 1;
        // when
        final var userProfile = UserProfile.of(username, tag);
        // then
        assertEquals(userProfile, UserProfile.of(username, tag));
    }

    @Test
    @DisplayName("Should consider Profiles of non-equal fields non-equal")
    void notEqualsTest() {
        // given
        final var username = "UserName";
        final var tag1 = 1;
        final var tag2 = tag1 + 1;
        // when
        final var userProfile = UserProfile.of(username, tag1);
        // then
        assertNotEquals(userProfile, UserProfile.of(username, tag2));
    }

    @Test
    @DisplayName("Should represent a profile as a string")
    void toStringTest() {
        // given
        final var username = "UserName";
        final var tag = 1;
        // when
        final var userProfile = UserProfile.of(username, tag);
        // then
        assertEquals(
                userProfile.toString(),
                "UserProfile{username='" + username + "', userTag=" + tag + "}"
        );
    }
}
