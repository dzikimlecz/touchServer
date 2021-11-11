package me.dzikimlecz.touchserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(MessageController.class)
class MessageControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MessageController controller;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        controller.clear();
    }

    @Test
    @DisplayName("Should get messages list")
    void fetchMessagesTest() throws Exception {
        // When
        mockMvc.perform(get("/touch/msg"))
        // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should send a message")
    void sendMessageTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        mockMvc.perform(post("/touch/msg")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(msg)))
        // Then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should fail to send same message twice")
    public void sendSameMessageTwiceTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        mockMvc.perform(post("/touch/msg")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msg)));
        mockMvc.perform(post("/touch/msg")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(msg)))
        // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should save sent messages")
    void saveMessageTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        final var msgSerialized = objectMapper.writeValueAsString(msg);
        mockMvc.perform(post("/touch/msg")
                .contentType(APPLICATION_JSON)
                .content(msgSerialized));
        mockMvc.perform(get("/touch/msg"))
        // Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json('[' + msgSerialized + ']')
                );
    }

    @Test
    @DisplayName("Should delete sent message")
    public void dropMessageTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        mockMvc.perform(post("/touch/msg")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msg)));
        mockMvc.perform(delete("/touch/msg")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(msg)))
        // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should fail to delete unsent message")
    public void dropUnsentMessageTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        mockMvc.perform(delete("/touch/msg")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(msg)))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fetch only messages to specified recipient")
    public void fetchMessagesToTest() throws Exception {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg1 = new Message(sender, recipient, "Come over.", now());
        final var msg1Serialized = objectMapper.writeValueAsString(msg1);
        final var msg2 = new Message(recipient, sender, "I can't. I'm playing League Of Legends", now());
        // When
        mockMvc.perform(post("/touch/msg")
                .contentType(APPLICATION_JSON)
                .content(msg1Serialized));
        mockMvc.perform(post("/touch/msg")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msg2)));
        mockMvc.perform(get("/touch/msg/" + recipient.getNameTag().replace('#', '_')))
                // Then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(1),
                        content().contentType(APPLICATION_JSON),
                        content().json('[' + msg1Serialized + ']')
                );

    }
}
