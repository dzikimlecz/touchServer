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
    @DisplayName("Should not throw on posting profile")
    void postProfileTest() {
        // when/then
        assertDoesNotThrow(
                () -> profileController.postProfile(new UserProfile("username", 0))
        );
    }

    @Test
    @DisplayName("Should throw on posting already posted profile")
    void postExistingProfileTest() {
        // given
        final var userProfile = new UserProfile("username", 0);
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
        final var userProfile = new UserProfile("username", 0);
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
        final var userProfile = new UserProfile("username", 0);
        // then
        assertThrows(
                NoSuchElementException.class,
                () -> profileController.deleteProfile(userProfile)
        );
    }

}
