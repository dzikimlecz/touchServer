package me.dzikimlecz.touchserver.service;

import me.dzikimlecz.touchserver.model.ElementAlreadyExistException;
import me.dzikimlecz.touchserver.model.UserProfile;
import me.dzikimlecz.touchserver.model.container.Container;
import me.dzikimlecz.touchserver.model.container.UserProfileContainer;
import me.dzikimlecz.touchserver.model.database.UserRepository;
import me.dzikimlecz.touchserver.model.database.entities.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;
import static me.dzikimlecz.touchserver.model.container.UserProfileContainer.wrapPage;

@Service
@Primary
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
        final var profile = UserProfile.of(username, tag);
        userRepository.findOne(Example.of(UserEntity.create(profile), ExampleMatcher.matching().withIgnoreNullValues()))
                .orElseThrow(() -> new NoSuchElementException(profile + "\nis not registered"));
        return profile;
    }

    @Override
    public void add(UserProfile profile) {
        final var entity = UserEntity.create(profile);
        userRepository.findOne(Example.of(entity, ExampleMatcher.matching().withIgnoreNullValues()))
                .ifPresent(__ -> { throw new ElementAlreadyExistException(profile + "\nis already registered"); });
        userRepository.save(entity);
    }

    @Override
    public UserProfile delete(UserProfile profile) {
        final var entity = userRepository
                .findOne(Example.of(UserEntity.create(profile), ExampleMatcher.matching().withIgnoreNullValues()))
                .orElseThrow(() -> new NoSuchElementException(profile + "\nis not registered"));
        userRepository.delete(entity);
        return profile;
    }

    @Override
    public Integer findId(UserProfile profile) {
        return userRepository
                .findOne(Example.of(UserEntity.create(profile), ExampleMatcher.matching().withIgnoreNullValues()))
                .orElseThrow(() -> new NoSuchElementException(profile + "\nis not registered"))
                .getId();
    }

    @Override
    public void reset() {
        userRepository.deleteAll();
    }

    @Override
    public Container<UserProfile> getProfilesPage(int page, int size) {
        return wrapPage(
                userRepository.findAll(PageRequest.of(page, size))
                .map(UserEntity::asProfile)
        );
    }
}
