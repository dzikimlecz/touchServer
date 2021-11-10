package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.*;
import static me.dzikimlecz.touchserver.model.UserProfile.getUsername;
import static me.dzikimlecz.touchserver.model.UserProfile.parseTag;

@Controller
@RequestMapping("/touch/msg/")
public class MessageController {
    private final List<Message> mockMessagesSource = new ArrayList<>();

    @GetMapping
    public List<Message> fetchMessages() {
        return List.copyOf(mockMessagesSource);
    }

    @GetMapping("/{nameTag}")
    public List<Message> fetchMessagesTo(@PathVariable String nameTag) {
        final var nameTagArr = nameTag.split("#");
        final var username = getUsername(nameTagArr);
        final long tag = parseTag(nameTagArr[1]);
        final var profile = UserProfile.of(username, tag);
        return mockMessagesSource.stream().collect(
                filtering(msg -> msg.getRecipient().equals(profile),
                        toList()
                ));
    }

    @PostMapping
    public void sendMessage(@RequestBody Message msg) {
        if (mockMessagesSource.contains(msg))
            throw new ElementAlreadyExistException();
        mockMessagesSource.add(msg);
    }

    @DeleteMapping
    public void dropMessage(@RequestBody Message msg) {
        if(mockMessagesSource.remove(msg)) return;
        throw new NoSuchElementException();
    }
}
