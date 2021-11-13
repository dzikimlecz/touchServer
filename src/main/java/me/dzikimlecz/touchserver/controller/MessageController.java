package me.dzikimlecz.touchserver.controller;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.Message;
import me.dzikimlecz.touchserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

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
    public ResponseEntity<String> notfound(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> notfound(ElementAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @GetMapping
    public Collection<Message> fetchMessages() {
        return messageService.fetchMessages();
    }

    @GetMapping("/{nameTag}")
    public Collection<Message> fetchMessagesTo(@PathVariable String nameTag) {
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
