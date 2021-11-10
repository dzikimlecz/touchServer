package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.*;
import static me.dzikimlecz.touchserver.model.UserProfile.getUsername;
import static me.dzikimlecz.touchserver.model.UserProfile.parseTag;

public class MessageController {
    private final List<Message> mockMessagesSource = new ArrayList<>();

    public List<Message> fetchMessages() {
        return List.copyOf(mockMessagesSource);
    }

    public List<Message> fetchMessagesTo(String nameTag) {
        final var nameTagArr = nameTag.split("#");
        final var username = getUsername(nameTagArr);
        final long tag = parseTag(nameTagArr[1]);
        final var profile = UserProfile.of(username, tag);
        return mockMessagesSource.stream().collect(
                filtering(msg -> msg.getRecipient().equals(profile),
                        toList()
                ));
    }

    public void sendMessage(Message msg) {
        if (mockMessagesSource.contains(msg))
            throw new ElementAlreadyExistException();
        mockMessagesSource.add(msg);
    }

    public void dropMessage(Message msg) {
        if(mockMessagesSource.remove(msg)) return;
        throw new NoSuchElementException();
    }
}
