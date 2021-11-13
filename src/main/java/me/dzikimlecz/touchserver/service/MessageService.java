package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.database.MessagesRepository;
import me.dzikimlecz.touchserver.model.database.entities.MessageEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public MessageService(MessagesRepository messagesRepository, UserProfileService userProfileService) {
        this.messagesRepository = messagesRepository;
        this.userProfileService = userProfileService;
    }

    public Collection<Message> fetchMessages() {
        return messagesRepository.findAll()
                .parallelStream()
                .map(this::getMessageOrDelete)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public Collection<Message> fetchMessagesTo(String nameTag) {
        UserProfile recipient = userProfileService.findByNameTag(nameTag);
        return fetchMessages().stream().filter(msg -> msg.getRecipient().equals(recipient)).collect(toList());
    }

    public void save(Message msg) {
        if (alreadySaved(msg))
            throw new ElementAlreadyExistException("Msg: " + msg + "\nalready exists");
        messagesRepository.save(getEntity(msg));
    }

    private boolean alreadySaved(@NotNull Message msg) {
        return messagesRepository.findAll()
                .stream()
                .map(this::getMessageOrDelete)
                .anyMatch(msg::equals);
    }

    public Message drop(Message msg) {
        if (alreadySaved(msg)) {
            delete(getEntity(msg));
            return msg;
        }
        throw new NoSuchElementException(msg + "\n is not saved");
    }

    public void reset() {
        messagesRepository.deleteAll();
    }

    @NotNull
    private MessageEntity getEntity(@NotNull Message msg) {
        return MessageEntity.create(msg, userProfileService.findId(msg.getSender()), userProfileService.findId(msg.getRecipient()));
    }

    @Nullable
    private Message getMessageOrDelete(MessageEntity entity) {
        final var sender = getProfileByIdOrElseDelete(entity.getSenderId(), entity);
        final var recipient = getProfileByIdOrElseDelete(entity.getRecipientId(), entity);
        if (sender.isEmpty() || recipient.isEmpty()) return null;
        return new Message(sender.get(), recipient.get(), entity.getContent(), entity.getSentOn());
    }

    private Optional<UserProfile> getProfileByIdOrElseDelete(Integer senderId, MessageEntity msg) {
        try {
            return Optional.of(userProfileService.findById(senderId));
        } catch (NoSuchElementException e) {
            delete(msg);
            return Optional.empty();
        }
    }

    private void delete(MessageEntity msg) {
        messagesRepository.delete(msg);
    }
}
