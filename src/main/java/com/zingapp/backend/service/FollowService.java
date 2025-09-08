package com.zingapp.backend.service;

import com.zingapp.backend.model.Follow;
import com.zingapp.backend.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void followUser(Long followerId, Long followingId) {
        Follow follow = new Follow(followerId, followingId);
        followRepository.save(follow);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public void unfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new RuntimeException("Follow not found"));
        followRepository.delete(follow);
    }

    public int getFollowCount(Long userID) {
        return followRepository.countByFollowingId(userID);
    }
}
