package me.dzikimlecz.touchserver.controller;


import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.container.Container;
import me.dzikimlecz.touchserver.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/touch/profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
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
    public Collection<UserProfile> getProfiles() {
        return userProfileService.getProfiles();
    }

    @GetMapping
    public Container<UserProfile> getProfilesPage(@RequestParam int page, @RequestParam int size) {
        if (size <= 0)
            throw new IllegalArgumentException(format("Can't construct page of size: %d", size));
        if (page < 0)
            throw new IllegalArgumentException(format("Can't construct page of negative index: %d", page));
        return userProfileService.getProfilesPage(page, size);
    }

    @GetMapping("/{nameTag}")
    public UserProfile getProfile(@PathVariable String nameTag) {
        return userProfileService.findByNameTag(nameTag);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void postProfile(@RequestBody UserProfile userProfile) {
        userProfileService.add(userProfile);
    }

    @DeleteMapping
    public UserProfile deleteProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.delete(userProfile);
    }

    protected void clear() {
        userProfileService.reset();
    }


}
