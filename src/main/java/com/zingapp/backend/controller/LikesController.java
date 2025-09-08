package com.zingapp.backend.controller;

import com.zingapp.backend.dto.LikeDTO;
import com.zingapp.backend.model.Likes;
import com.zingapp.backend.model.Post;
import com.zingapp.backend.model.User;
import com.zingapp.backend.repository.PostRepository;
import com.zingapp.backend.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public ResponseEntity<?> likePost(@RequestBody LikeDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getUserId().equals(dto.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You cannot like your own post.");
        }

        Likes like = new Likes();

        like.setPost(post);

        User user = new User();
        user.setId(dto.getUserId());
        like.setUser(user);

        Likes saved = likesService.likePost(like);

        LikeDTO response = new LikeDTO(
                saved.getId(),
                saved.getPost().getId(),
                saved.getUser().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeDTO>> getLikesByPost(@PathVariable Long postId) {
        List<LikeDTO> likes = likesService.getLikesByPost(postId)
                .stream()
                .map(like -> new LikeDTO(
                        like.getId(),
                        like.getPost().getId(),
                        like.getUser().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(likes);
    }

    @DeleteMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Void> unlikePostByPostAndUser(@PathVariable Long postId, @PathVariable Long userId) {
        Likes like = likesService.findByPostAndUser(postId, userId);
        if (like != null) {
            likesService.unlikePost(like.getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
