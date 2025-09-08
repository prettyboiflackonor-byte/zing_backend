package com.zingapp.backend.controller;

import com.zingapp.backend.dto.CommentDTO;
import com.zingapp.backend.model.Comments;
import com.zingapp.backend.model.Post;
import com.zingapp.backend.model.Profile;
import com.zingapp.backend.model.User;
import com.zingapp.backend.repository.ProfileRepository;
import com.zingapp.backend.service.CommentsService;
import com.zingapp.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public CommentsController(
            CommentsService commentsService,
            UserRepository userRepository,
            ProfileRepository profileRepository
    ) {
        this.commentsService = commentsService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }


    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO dto) {
        if (dto.getComment() != null && dto.getComment().length() > 200) {
            throw new IllegalArgumentException("Comment too long");
        }

        Comments comment = new Comments();
        comment.setComment(dto.getComment());

        Post post = new Post();
        post.setId(dto.getPostId());
        comment.setPost(post);

        User user = new User();
        user.setId(dto.getUserId());
        comment.setUser(user);

        Comments saved = commentsService.addComment(comment);

        // Hent user og profile fra databasen
        User fullUser = userRepository.findById(saved.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = profileRepository.findByUserId(fullUser.getId()).orElse(null);

        Long profileId = profile != null ? profile.getId() : null;
        String profilePicUrl = (profile != null && profile.getProfileImage() != null)
                ? profile.getProfileImage().getUrl()
                : null;

        CommentDTO response = new CommentDTO(
                saved.getId(),
                saved.getPost().getId(),
                saved.getUser().getId(),
                saved.getComment(),
                fullUser.getFirstName(),
                fullUser.getLastName(),
                profileId,
                profilePicUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentsService.getCommentsByPost(postId)
                .stream()
                .map(c -> {
                    Profile profile = c.getUser().getProfile();
                    Long profileId = profile != null ? profile.getId() : null;
                    String profilePicUrl = (profile != null && profile.getProfileImage() != null)
                            ? profile.getProfileImage().getUrl()
                            : null;

                    return new CommentDTO(
                            c.getId(),
                            c.getPost().getId(),
                            c.getUser().getId(),
                            c.getComment(),
                            c.getUser().getFirstName(),
                            c.getUser().getLastName(),
                            profileId,
                            profilePicUrl
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();

    }
}