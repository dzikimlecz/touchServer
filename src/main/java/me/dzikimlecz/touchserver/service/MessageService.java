package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.container.Container;

import java.util.Collection;


public interface MessageService {
    Collection<Message> fetchMessages();

    Collection<Message> fetchMessagesTo(String nameTag);

    void save(Message msg);

    Message drop(Message msg);

    void reset();

    Container<Message> retrieveMessagesTo(String nameTag, int page, int size);

    Container<Message> retrieveMessagesPage(int page, int size);
}
