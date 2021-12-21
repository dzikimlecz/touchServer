package me.dzikimlecz.touchserver.model.container;


import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public final class MessageContainer extends Container<Message> {

    private final List<Message> elements;

    public MessageContainer(int index, int numberOfPages, List<Message> elements) {
        super(index, numberOfPages);
        this.elements = List.copyOf(elements);
    }

    @Override
    public List<Message> getElements() {
        return elements;
    }

    @Contract("_ -> new")
    public static @NotNull MessageContainer wrapPage(@NotNull Page<Message> page) {
        return new MessageContainer(page.getNumber(), page.getTotalPages(), page.getContent());
    }
}
