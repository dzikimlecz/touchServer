package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class ProfileControllerTest {
    final ProfileController profileController = new ProfileController();

    @Test
    @DisplayName("Gets any List")
    void getAnyProfilesListTest() {
        // when
        final var response = profileController.getProfiles();
        // then
        assertNotNull(response);
    }

    @Test
    @DisplayName("Gets List of profiles")
    void getProfilesListTest() {
        // when
        final var response = profileController.getProfiles();
        // then
        assertFalse(response.isEmpty());
    }

    @Test
    @DisplayName("Should get profile of specified name and tag")
    void getSpecificProfileTest() {
        // given
        final var username = "UserName";
        final var tag = 100;
        final var profile = UserProfile.of(username, tag);
        // when
        profileController.postProfile(profile);
        // then
        assertEquals(profile, profileController.getProfile(username + '#' + tag));
    }

    @Test
    @DisplayName("Should fail to get profile of non-existent NameTag")
    void getNonExistentProfileTest() {
        assertThrows(
                NoSuchElementException.class,
                () -> profileController.getProfile("Hello#2")
        );
    }

    @Test
    @DisplayName("Should not throw on posting profile")
    void postProfileTest() {
        // when/then
        assertDoesNotThrow(
                () -> profileController.postProfile(UserProfile.of("username", 1))
        );
    }

    @Test
    @DisplayName("Should throw on posting already posted profile")
    void postExistingProfileTest() {
        // given
        final var userProfile = UserProfile.of("username", 1);
        // when
        profileController.postProfile(userProfile);
        // then
        assertThrows(
                ElementAlreadyExistException.class,
                () -> profileController.postProfile(userProfile)
        );
    }

    @Test
    @DisplayName("Should delete existing profile profile")
    void deleteProfileTest() {
        // given
        final var userProfile = UserProfile.of("username", 1);
        // when
        profileController.postProfile(userProfile);
        // then
        assertDoesNotThrow(
                () -> profileController.deleteProfile(userProfile)
        );
    }

    @Test
    @DisplayName("Should throw on deleting absent profile")
    void deleteAbsentProfileTest() {
        // when
        final var userProfile = UserProfile.of("username", 1);
        // then
        assertThrows(
                NoSuchElementException.class,
                () -> profileController.deleteProfile(userProfile)
        );
    }

}
