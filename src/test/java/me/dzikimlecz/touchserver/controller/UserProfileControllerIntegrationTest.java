package me.dzikimlecz.touchserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
public class UserProfileControllerIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserProfileController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String uri = "/touch/profiles";

    @BeforeEach
    void setUp() {
        controller.clear();
    }

    @Test
    @DisplayName("Gets any List")
    void getAnyProfilesListTest() throws Exception {
        // when
        mockMvc.perform(get(uri))
        // then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").isNotEmpty()
                );
    }

    @Test
    @DisplayName("Gets List of profiles")
    void getProfilesListTest() throws Exception {
        /// when
        mockMvc.perform(get(uri))
        // then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0]").isNotEmpty()
                );
    }

    @Test
    @DisplayName("Should get profile of specified name and tag")
    void getSpecificProfileTest() throws Exception {
        // given
        final var username = "UserName";
        final var tag = 100;
        final var profile = UserProfile.of(username, tag);
        // when
        final var profileJson = objectMapper.writeValueAsString(profile);
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON)
                .content(profileJson));
        mockMvc.perform(get(uri + '/' + username + '_' + tag))
        // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(profileJson)
                );
    }

    @Test
    @DisplayName("Should fail to get profile of non-existent NameTag")
    void getNonExistentProfileTest() throws Exception {
        // when
        mockMvc.perform(get(uri + "Null_9"))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should post a profile")
    void postProfileTest() throws Exception {
        //given
        final var profile = UserProfile.of("new", 1);
        final var profileJson = objectMapper.writeValueAsString(profile);
        // when
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON)
                .content(profileJson))
        // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should throw on posting already posted profile")
    void postExistingProfileTest() throws Exception {
        // given
        final var profile = UserProfile.of("username", 1);
        final var profileJson = objectMapper.writeValueAsString(profile);
        // when
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON)
                .content(profileJson));
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON)
                .content(profileJson))
        // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete existing profile profile")
    void deleteProfileTest() throws Exception {
        // given
        final var profile = UserProfile.of("username", 1);
        final var profileJson = objectMapper.writeValueAsString(profile);
        // when
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON)
                .content(profileJson));
        mockMvc.perform(delete(uri)
                        .contentType(APPLICATION_JSON)
                        .content(profileJson))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should throw on deleting absent profile")
    void deleteAbsentProfileTest() throws Exception {
        // given
        final var profile = UserProfile.of("NuLl", 19);
        final var profileJson = objectMapper.writeValueAsString(profile);
        // when
        mockMvc.perform(delete(uri)
                        .contentType(APPLICATION_JSON)
                        .content(profileJson))
                // then
                .andExpect(status().isNotFound());
    }
}
