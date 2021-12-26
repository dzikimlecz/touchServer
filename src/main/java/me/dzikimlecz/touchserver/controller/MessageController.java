package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.model.container.Container;
import me.dzikimlecz.touchserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/touch/msg")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notFound(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> alreadyExists(ElementAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @GetMapping("/all")
    public Collection<Message> fetchAllMessages() {
        return messageService.fetchMessages();
    }

    @GetMapping("/{nameTag}")
    public Container<Message> fetchMessagesTo(
            @PathVariable String nameTag,
            @RequestParam int page,
            @RequestParam int size
    )  {
        if (size <= 0)
            throw new IllegalArgumentException(format("Can't construct page of size: %d", size));
        if (page < 0)
            throw new IllegalArgumentException(format("Can't construct page of negative index: %d", page));
        return messageService.retrieveMessagesTo(nameTag, page, size);
    }

    @GetMapping()
    public Container<Message> fetchMessagesBetween(
            @RequestParam String user1,
            @RequestParam String user2,
            @RequestParam int page,
            @RequestParam int size
    )  {
        if (size <= 0)
            throw new IllegalArgumentException(format("Can't construct page of size: %d", size));
        if (page < 0)
            throw new IllegalArgumentException(format("Can't construct page of negative index: %d", page));
        return messageService.retrieveMessagesOfUsers(user1, user2, page, size);
    }

    @GetMapping("/all/{nameTag}")
    public Collection<Message> fetchAllMessagesTo(@PathVariable String nameTag) {
        return messageService.fetchMessagesTo(nameTag);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void sendMessage(@RequestBody Message msg) {
        messageService.save(msg);
    }

    @DeleteMapping
    public Message dropMessage(@RequestBody Message msg) {
        return messageService.drop(msg);
    }

    protected void clear() {
        messageService.reset();
    }
}
