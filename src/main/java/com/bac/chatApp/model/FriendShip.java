package com.bac.chatApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendships",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_friendships_user_pair",
                columnNames = {"user_a_id", "user_b_id"}
        ))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendShip implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_a_id", nullable = false)
    private Long userAId;

    @Column(name = "user_b_id", nullable = false)
    private Long userBId;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FriendshipStatus status;

    @Column(name = "message", length = 300)
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods để đảm bảo user_a_id luôn nhỏ hơn user_b_id
    @PrePersist
    @PreUpdate
    private void normalizeFriendship() {
        if (userAId != null && userBId != null && userAId > userBId) {
            Long temp = userAId;
            userAId = userBId;
            userBId = temp;
        }
    }

}
