package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.*;
import static me.dzikimlecz.touchserver.model.UserProfile.getUsername;
import static me.dzikimlecz.touchserver.model.UserProfile.parseTag;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/touch/msg")
public class MessageController {
    private final List<Message> mockMessagesSource = new ArrayList<>();

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notfound(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> notfound(ElementAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @GetMapping
    public Collection<Message> fetchMessages() {
        return List.copyOf(mockMessagesSource);
    }

    @GetMapping("/{nameTag}")
    public Collection<Message> fetchMessagesTo(@PathVariable String nameTag) {
        final var nameTagArr = nameTag.split("_");
        final var username = getUsername(nameTagArr);
        final long tag = parseTag(nameTagArr[1]);
        final var profile = UserProfile.of(username, tag);
        return mockMessagesSource.stream().collect(
                filtering(msg -> msg.getRecipient().equals(profile),
                        toList()
                ));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void sendMessage(@RequestBody Message msg) {
        if (mockMessagesSource.contains(msg))
            throw new ElementAlreadyExistException("Msg: " + msg + "\nalready exists");
        mockMessagesSource.add(msg);
    }

    @DeleteMapping
    public Message dropMessage(@RequestBody Message msg) {
        if(mockMessagesSource.remove(msg)) return null;
        throw new NoSuchElementException("Msg: " + msg + "\n does not exist");
    }

    protected void clear() {
        mockMessagesSource.clear();
    }
}
