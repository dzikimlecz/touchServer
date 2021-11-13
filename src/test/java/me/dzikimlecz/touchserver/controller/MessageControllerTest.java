package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.service.MockMessagesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {

    private final MessageController messageController = new MessageController(new MockMessagesService());


    @Test
    @DisplayName("Should get a list of messages")
    void fetchMessagesTest() {
        // when
        final var response = messageController.fetchMessages();
        // then
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should send messages")
    public void sendMessageTest() {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        // When
        final var msg = new Message(sender, recipient, "Come over.", now());
        // Then
        assertDoesNotThrow(() -> messageController.sendMessage(msg));
    }

    @Test
    @DisplayName("Should fail to send same message twice")
    public void sendSameMessageTwiceTest() {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        messageController.sendMessage(msg);
        // Then
        assertThrows(
                ElementAlreadyExistException.class, () -> messageController.sendMessage(msg)
        );
    }

    @Test
    @DisplayName("Should save sent messages")
    void saveMessageTest() {
        // given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // when
        messageController.sendMessage(msg);
        final var response = messageController.fetchMessages();
        // then
        assertTrue(response.contains(msg));
    }

    @Test
    @DisplayName("Should delete sent message")
    public void dropMessageTest() {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg = new Message(sender, recipient, "Come over.", now());
        // When
        messageController.sendMessage(msg);
        // Then
        assertDoesNotThrow(() -> messageController.dropMessage(msg));
    }

    @Test
    @DisplayName("Should fail to delete unsent message")
    public void dropUnsentMessageTest() {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        // When/Then
        assertThrows(NoSuchElementException.class, () ->
                messageController.dropMessage(new Message(sender, recipient, "Come over.", now()))
        );
    }

    @Test
    @DisplayName("Should fetch only messages to specified recipient")
    public void fetchMessagesToTest() {
        // Given
        final var sender = UserProfile.of("sender", 1);
        final var recipient = UserProfile.of("recipient", 1);
        final var msg1 = new Message(sender, recipient, "Come over.", now());
        final var msg2 = new Message(recipient, sender, "I can't. I'm playing League Of Legends", now());
        // When
        messageController.sendMessage(msg1);
        messageController.sendMessage(msg2);
        final var response =
                messageController.fetchMessagesTo(recipient.getNameTag());
        // Then
        assertTrue(response.contains(msg1));
        assertFalse(response.contains(msg2));
    }

}
