package me.dzikimlecz.touchserver.model.database;

import me.dzikimlecz.touchserver.model.database.entities.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<MessageEntity, Integer> {
    Page<MessageEntity> findByRecipientId(Integer RecipientID, Pageable pageable);
    Page<MessageEntity> findBySenderId(Integer SenderId, Pageable pageable);
    @Query("select m from MessageEntity m " +
            "where (m.recipientId = ?1 and m.senderId = ?2) or (m.recipientId = ?2 and m.senderId = ?1)")
    Page<MessageEntity> findByUserIds(Integer id1, Integer id2, Pageable pageable);
    Page<MessageEntity> findByRecipientIdOrSenderId(Integer RecipientID, Integer SenderId, Pageable pageable);

}
