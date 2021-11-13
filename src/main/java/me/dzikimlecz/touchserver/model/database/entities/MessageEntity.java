package me.dzikimlecz.touchserver.model.database.entities;


import me.dzikimlecz.touchserver.model.Message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class MessageEntity {
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

    public static MessageEntity create(Message msg, Integer senderId, Integer recipientId) {
        final var entity = new MessageEntity();
        entity.content = msg.getContent();
        entity.recipientId = recipientId;
        entity.senderId = senderId;
        entity.sentOn = msg.getSentOn();
        return entity;
    }
}
