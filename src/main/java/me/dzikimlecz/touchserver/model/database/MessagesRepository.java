package me.dzikimlecz.touchserver.model.database;

import me.dzikimlecz.touchserver.model.database.entities.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<MessageEntity, Integer> {
    Page<MessageEntity> findByRecipientId(Integer RecipientID, Pageable pageable);

}
