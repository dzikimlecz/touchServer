package me.dzikimlecz.touchserver.controller;


import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/touch/profiles")
public class ProfileController {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notfound(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> notfound(ElementAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    private final List<UserProfile> mockUserSource = new ArrayList<>();
    public ProfileController() { mockUserSource.add(UserProfile.of("username", 1)); }

    @GetMapping
    public List<UserProfile> getProfiles() {
        return List.copyOf(mockUserSource);
    }

    @GetMapping("/{nameTag}")
    public UserProfile getProfile(@PathVariable String nameTag) {
        final var nameTagArr = nameTag.split("_");
        final var username = UserProfile.getUsername(nameTagArr);
        final long tag = UserProfile.parseTag(nameTagArr[1]);

        for (UserProfile userProfile : mockUserSource)
            if (tag == userProfile.getUserTag() && username.equals(userProfile.getUsername()))
                return userProfile;

        throw new NoSuchElementException(String.format("No Profile of name='%s' and tag=%d", username, tag));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void postProfile(@RequestBody UserProfile userProfile) {
        if (mockUserSource.contains(userProfile))
            throw new ElementAlreadyExistException(userProfile.toString() + " already exists.");
        mockUserSource.add(userProfile);
    }

    @DeleteMapping
    public void deleteProfile(@RequestBody UserProfile userProfile) {
        if(mockUserSource.remove(userProfile)) return;
        throw new NoSuchElementException(String.format("Profile %s \n does not exist", userProfile));
    }

    protected void clear() {
        mockUserSource.clear();
        mockUserSource.add(UserProfile.of("username", 1));
    }


}
