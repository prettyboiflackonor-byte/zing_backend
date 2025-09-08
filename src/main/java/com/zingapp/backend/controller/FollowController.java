package com.zingapp.backend.controller;

import com.zingapp.backend.service.FollowService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{followingId}")
    public void followUser(@PathVariable Long followingId, @RequestParam Long followerId) {
        followService.followUser(followerId, followingId);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<?> unfollowUser(@RequestParam Long followerId, @PathVariable Long followingId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isFollowing")
    public boolean isFollowing(@RequestParam Long followerId, @RequestParam Long followingId) {
        return followService.isFollowing(followerId, followingId);
    }

    @GetMapping("/count/{userId}")
    public int countFollowers(@PathVariable Long userId) {
        return followService.getFollowCount(userId);
    }
}
