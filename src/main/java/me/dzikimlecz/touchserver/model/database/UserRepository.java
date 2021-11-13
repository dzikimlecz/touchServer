package me.dzikimlecz.touchserver.model.database;

import me.dzikimlecz.touchserver.model.database.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {  }
