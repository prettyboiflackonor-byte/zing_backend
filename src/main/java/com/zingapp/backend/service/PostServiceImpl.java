package com.zingapp.backend.service;

import com.zingapp.backend.dto.PostDTO;
import com.zingapp.backend.dto.PostResponseDTO;
import com.zingapp.backend.model.Image;
import com.zingapp.backend.model.Post;
import com.zingapp.backend.repository.*;
import com.zingapp.backend.repository.CommentsRepository;
import com.zingapp.backend.repository.LikesRepository;
import com.zingapp.backend.model.User;
import com.zingapp.backend.model.Profile;
import java.util.ArrayList;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CommentsRepository commentsRepository;
    private final LikesRepository likesRepository;

    public PostServiceImpl(PostRepository postRepository, ImageRepository imageRepository,
                           UserRepository userRepository, ProfileRepository profileRepository,
                           CommentsRepository commentsRepository, LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.commentsRepository = commentsRepository;
        this.likesRepository = likesRepository;
    }

    @Override
    public Post createPost(PostDTO dto) {
        Post post = new Post();

        if (dto.getImageId() != null) {
            imageRepository.findById(dto.getImageId()).ifPresent(post::setImage);
        }

        post.setCaption(dto.getCaption());
        post.setUserId(dto.getUserId());
        post.setDate(LocalDateTime.now());


        Post savedPost = postRepository.save(post);

        if (savedPost.getImage() != null) {
            savedPost.getImage().setPost(savedPost);
            imageRepository.save(savedPost.getImage());
        }

        return savedPost;
    }



    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllOrderedByDateDesc();
        List<PostResponseDTO> result = new ArrayList<>();

        for (Post post : posts) {
            // Hent tilknyttet bruker og profil
            User user = userRepository.findById(post.getUserId()).orElse(null);
            Profile profile = profileRepository.findByUserId(post.getUserId()).orElse(null);

            // Ekstraheres med nullsjekk
            String firstName = user != null ? user.getFirstName() : "";
            String lastName = user != null ? user.getLastName() : "";

            String profilePicUrl = profile != null && profile.getProfileImage() != null
                    ? profile.getProfileImage().getUrl()
                    : "";

            Long imageId = post.getImage() != null ? post.getImage().getId() : null;
            String imageUrl = post.getImage() != null ? post.getImage().getUrl() : null;

            Long profileId = profile != null ? profile.getId() : null;


            result.add(new PostResponseDTO(
                    post.getId(),
                    post.getCaption(),
                    imageId,
                    post.getDate(),
                    firstName,
                    lastName,
                    profilePicUrl,
                    imageUrl,
                    profileId,
                    post.getUserId()
            ));
        }


        return result;
    }



    @Override
    public List<PostResponseDTO> getPostsByUserID(Long userId) {
        List<Post> posts = postRepository.findByUserIdOrderByDateDesc(userId);
        List<PostResponseDTO> result = new ArrayList<>();

        // Hent bruker og profil én gang siden userId er lik for alle postene
        User user = userRepository.findById(userId).orElse(null);
        Profile profile = profileRepository.findByUserId(userId).orElse(null);

        String firstName = user != null ? user.getFirstName() : "";
        String lastName = user != null ? user.getLastName() : "";

        String profilePicUrl = profile != null && profile.getProfileImage() != null
                ? profile.getProfileImage().getUrl()
                : "";

        for (Post post : posts) {
            Long imageId = post.getImage() != null ? post.getImage().getId() : null;
            String imageUrl = post.getImage() != null ? post.getImage().getUrl() : null;
            Long profileId = profile != null ? profile.getId() : null;


            result.add(new PostResponseDTO(
                    post.getId(),
                    post.getCaption(),
                    imageId,
                    post.getDate(),
                    firstName,
                    lastName,
                    profilePicUrl,
                    imageUrl,
                    profileId,
                    post.getUserId()
            ));
        }

        return result;
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this post");
        }

        // Slett koblingen til image
        if (post.getImage() != null) {
            Image image = post.getImage();
            post.setImage(null);
            postRepository.save(post); // oppdaterer Posten sånn at Hibernate ikke tror relasjonen fortsatt finnes
            imageRepository.delete(image);
        }


        // Slett kommentarer og likes
        commentsRepository.deleteByPost_Id(postId);
        likesRepository.deleteByPost_Id(postId);

        // Slett posten til slutt
        postRepository.deleteById(postId);
    }


}
