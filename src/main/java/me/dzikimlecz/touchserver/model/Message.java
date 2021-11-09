package me.dzikimlecz.touchserver.model;

import org.jetbrains.annotations.NotNull;

public final class Message {
    public UserProfile getSender() {
        return sender;
    }
    private final UserProfile sender;

    public UserProfile getRecipient() {
        return recipient;
    }
    private final UserProfile recipient;

    public String getContent() {
        return content;
    }
    private final String content;

    public Message(
            @NotNull UserProfile sender,
            @NotNull UserProfile recipient,
            @NotNull String content
    ) {
        this.sender = sender;
        this.recipient = recipient;
        if (content.isEmpty())
            throw new IllegalArgumentException("Content of the message can't be empty.");
        this.content = content;
    }


}
