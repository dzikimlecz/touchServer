package me.dzikimlecz.touchserver.service;


import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.container.Container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

public class MockMessagesService implements MessageService {

    private final List<Message> messages = new ArrayList<>();

    @Override
    public Collection<Message> fetchMessages() {
        return List.copyOf(messages);
    }

    @Override
    public Collection<Message> fetchMessagesTo(String nameTag) {
        return messages.stream()
                .filter(msg -> nameTag.equals(msg.getRecipient().getNameTag()))
                .collect(toList());
    }

    @Override
    public void save(Message msg) {
        if (messages.contains(msg))
            throw new ElementAlreadyExistException();
        messages.add(msg);
    }

    @Override
    public Message drop(Message msg) {
        if (messages.remove(msg))
            return msg;
        throw new NoSuchElementException();
    }

    @Override
    public void reset() {
        messages.clear();
    }

    @Override
    public Container<Message> retrieveMessagesTo(String nameTag, int page, int size) {
        return null;
    }

    @Override
    public Container<Message> retrieveMessagesPage(int page, int size) {
        return null;
    }

}
