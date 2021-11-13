package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.database.UserRepository;
import me.dzikimlecz.touchserver.model.database.entities.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;

    @Autowired
    public UserProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<UserProfile> getProfiles() {
        return userRepository.findAll()
                .parallelStream()
                .map(UserEntity::asProfile)
                .collect(toList());
    }

    @Override
    public UserProfile findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No user of id: " + id)).asProfile();
    }

    @Override
    public UserProfile findByNameTag(@NotNull String nameTag) {
        final var nameTagArr = nameTag.split("_");
        final var username = UserProfile.getUsername(nameTagArr);
        final var tag = UserProfile.parseTag(nameTagArr[1]);
        final var entities = userRepository.findAll();
        for (UserEntity entity : entities)
            if (entity.getUserTag() == tag && entity.getUsername().equals(username))
                return entity.asProfile();
        throw new NoSuchElementException("No user of nameTag: " + nameTag);
    }

    @Override
    public void add(UserProfile profile) {
        final var entities = userRepository.findAll();
        for (UserEntity entity : entities)
            if (entity.asProfile().equals(profile))
                throw new ElementAlreadyExistException(profile + "\nis already registered");
        userRepository.save(new UserEntity(profile));
    }

    @Override
    public UserProfile delete(UserProfile profile) {
        final var entities = userRepository.findAll();
        for (UserEntity entity : entities) {
            if (entity.asProfile().equals(profile)) {
                userRepository.delete(entity);
                return profile;
            }
        }
        throw new NoSuchElementException(profile + "\nis not registered");
    }

    @Override
    public Integer findId(UserProfile profile) {
        final var entities = userRepository.findAll();
        for (UserEntity entity : entities)
            if (entity.asProfile().equals(profile))
                return entity.getId();
        throw new NoSuchElementException(profile + "\nis not registered");
    }

    @Override
    public void reset() {
        userRepository.deleteAll();
        // todo: remove in production, only for testing reasons
            userRepository.save(new UserEntity(UserProfile.of("username", 1)));
    }
}
