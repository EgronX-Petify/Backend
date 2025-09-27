package com.example.petify.repository.profile;

import com.example.petify.model.profile.Notification;
import com.example.petify.model.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByRecipientOrderByCreatedAtDesc(Profile recipient, Pageable pageable);
    
    Page<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(Profile recipient, Pageable pageable);

    long countByRecipientAndIsReadFalse(Profile recipient);
    long countByRecipient(Profile recipient);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId AND n.recipient.id = :recipientId")
    int markAsRead(@Param("notificationId") Long notificationId, @Param("recipientId") Long recipientId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.id = :recipientId AND n.isRead = false")
    int markAllAsRead(@Param("recipientId") Long recipientId);
    
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.createdAt < :cutoffDate")
    void deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);


}