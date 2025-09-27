package com.example.petify.domain.profile.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Profile recipient;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private NotificationType type = NotificationType.GENERAL;
}
