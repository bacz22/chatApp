package com.bac.chatApp.repository;

import com.bac.chatApp.model.FriendShip;
import com.bac.chatApp.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    @Query("SELECT f FROM FriendShip f WHERE " +
            "(f.userAId = :userId1 AND f.userBId = :userId2) OR " +
            "(f.userAId = :userId2 AND f.userBId = :userId1)")
    Optional<FriendShip> findByUserPair(@Param("userId1") Long userId1,
                                        @Param("userId2") Long userId2);

    @Query("select f from FriendShip f where (f.userAId = :userId or f.userBId = :userId) and f.status = :status")
    List<FriendShip> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status")FriendshipStatus status);

    //Lời mời đã nhận
    @Query("SELECT f FROM FriendShip f WHERE (f.userAId = :userId OR f.userBId = :userId) AND f.requesterId <> :userId AND f.status = 'PENDING'")
    List<FriendShip> findPendingRequestsForUser(@Param("userId") Long userId);

    // Lời mời đã gửi
    @Query("SELECT f FROM FriendShip f WHERE f.requesterId = :userId AND f.status = 'PENDING'")
    List<FriendShip> findSentRequests(@Param("userId") Long userId);
}
