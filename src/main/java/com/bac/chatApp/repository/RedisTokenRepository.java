package com.bac.chatApp.repository;

import com.bac.chatApp.model.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
    // Tìm theo jwtId
    Optional<RedisToken> findByJwtId(String jwtId);

    // logout all
    List<RedisToken> findByUserId(String userId);

    // Xóa tất cả token của user
    void deleteByUserId(String userId);
}
