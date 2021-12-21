package me.dzikimlecz.touchserver.model.database.entities;


import me.dzikimlecz.touchserver.model.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class MessageEntity implements Comparable<MessageEntity> {
    @GeneratedValue(strategy= GenerationType.AUTO)
    private @Id Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String content;
    private LocalDateTime sentOn;

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(LocalDateTime sentOn) {
        this.sentOn = sentOn;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public MessageEntity() {}

    @Contract("_, _, _ -> new")
    public static @NotNull MessageEntity create(Message msg, Integer senderId, Integer recipientId) {
        final var entity = new MessageEntity();
        if (msg != null) {
            entity.content = msg.getContent();
            entity.sentOn = msg.getSentOn().minusNanos(msg.getSentOn().getNano());
        }
        entity.recipientId = recipientId;
        entity.senderId = senderId;
        return entity;
    }

    @Override
    public int compareTo(@NotNull MessageEntity o) {
        return -sentOn.compareTo(o.sentOn);
    }
}
