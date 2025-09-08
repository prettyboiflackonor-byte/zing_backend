package com.zingapp.backend.repository;

import com.zingapp.backend.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    int countByFollowingId(Long followingId);
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
