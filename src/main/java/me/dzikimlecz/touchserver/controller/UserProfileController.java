package me.dzikimlecz.touchserver.controller;


import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

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
    public ResponseEntity<String> notfound(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> notfound(ElementAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @GetMapping
    public Collection<UserProfile> getProfiles() {
        return userProfileService.getProfiles();
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
