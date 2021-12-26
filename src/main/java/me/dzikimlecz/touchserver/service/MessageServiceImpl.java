package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.container.Container;
import me.dzikimlecz.touchserver.model.container.MessageContainer;
import me.dzikimlecz.touchserver.model.database.MessagesRepository;
import me.dzikimlecz.touchserver.model.database.entities.MessageEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.ceil;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static me.dzikimlecz.touchserver.model.container.MessageContainer.wrapPage;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.domain.Sort.sort;

@Service
@Primary
public class MessageServiceImpl implements MessageService {
    private final MessagesRepository messagesRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public MessageServiceImpl(MessagesRepository messagesRepository, UserProfileService userProfileService) {
        this.messagesRepository = messagesRepository;
        this.userProfileService = userProfileService;
    }

    @Override
    public Collection<Message> fetchMessages() {
        return messagesRepository.findAll()
                .parallelStream()
                .map(this::getMessageOrDelete)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Override
    public Collection<Message> fetchMessagesTo(String nameTag) {
        var id = userProfileService.findId(userProfileService.findByNameTag(nameTag));
        return messagesRepository.findAll(Example.of(MessageEntity.create(null, null, id), matchingAny()))
                .stream().map(this::getMessageOrDelete)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Override
    public void save(Message msg) {
        messagesRepository.save(getEntity(msg));
    }

    private boolean alreadySaved(@NotNull Message msg) {
        return messagesRepository.findAll()
                .stream()
                .map(this::getMessageOrDelete)
                .anyMatch(msg::equals);
    }

    @Override
    public Message drop(Message msg) {
        if (alreadySaved(msg)) {
            delete(getEntity(msg));
            return msg;
        }
        throw new NoSuchElementException(msg + "\n is not saved");
    }

    @Override
    public void reset() {
        messagesRepository.deleteAll();
    }

    @Override
    public Container<Message> retrieveMessagesTo(String nameTag, int page, int size) {
        final Integer id = userProfileService.findId(userProfileService.findByNameTag(nameTag));
        final var messageEntities = messagesRepository
                .findByRecipientId(id, PageRequest.of(page, size, sort(MessageEntity.class)))
                .map(this::getMessageOrDelete);
        return wrapPage(messageEntities);
    }

    @Override
    public Container<Message> retrieveMessagesPage(int page, int size) {
        return wrapPage(
                messagesRepository.findAll(PageRequest.of(page, size, sort(MessageEntity.class)))
                .map(this::getMessageOrDelete)
        );
    }

    @Override
    public Container<Message> retrieveMessagesOfUsers(String user1, String user2, int page, int size) {
        var id1 = userProfileService.findId(userProfileService.findByNameTag(user1));
        var id2 = userProfileService.findId(userProfileService.findByNameTag(user2));
        var resultPage1 =
                messagesRepository.findByRecipientIdAndSenderId(id1, id2, PageRequest.of(page, size))
                        .map(this::getMessageOrDelete).getContent();
        var resultPage2 =
                messagesRepository.findByRecipientIdAndSenderId(id2, id1, PageRequest.of(page, size))
                        .map(this::getMessageOrDelete).getContent();
        final var size1 = resultPage1.size();
        final var size2 = resultPage2.size();
        List<Message> resultList = new ArrayList<>();
        var halfSize = (int) ceil(size / 2.0);
        if (size1 >= halfSize && size2 >= halfSize) {
            resultList.addAll(resultPage1.subList(0, halfSize));
            resultList.addAll(resultPage2.subList(0, halfSize));
        } else if (size1 < halfSize && size2 < halfSize) {
            resultList.addAll(resultPage1);
            resultList.addAll(resultPage2);
        } else if (size1 >= halfSize) {
            resultList.addAll(resultPage1.subList(0, (2 * halfSize) - size2));
            resultList.addAll(resultPage2);
        } else {
            resultList.addAll(resultPage1);
            resultList.addAll(resultPage2.subList(0, (2 * halfSize) - size1));
        }
        if (resultList.size() > size) resultList = resultList.subList(0, size);
        return new MessageContainer(page, size, resultList);
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
