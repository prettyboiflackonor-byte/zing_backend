package com.zingapp.backend.controller;

import com.zingapp.backend.dto.CommentDTO;
import com.zingapp.backend.dto.PostDTO;
import com.zingapp.backend.dto.PostResponseDTO;
import com.zingapp.backend.model.Post;
import com.zingapp.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Opprett nytt innlegg
    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostDTO dto) {
        Post savedPost = postService.createPost(dto);
        return ResponseEntity.ok(savedPost);
    }

    @DeleteMapping("/{postId}/user/{userId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @PathVariable Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // Hent alle innlegg med brukerinfo og profilbilde
    @GetMapping("/details")
    public ResponseEntity<List<PostResponseDTO>> getAllPostDetails() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Hent innlegg for en spesifikk bruker
    @GetMapping("/details/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getUserPosts(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getPostsByUserID(userId);
        return ResponseEntity.ok(posts);
    }
}
