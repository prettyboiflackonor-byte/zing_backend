package com.zingapp.backend.service;

import com.zingapp.backend.model.Likes;
import com.zingapp.backend.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {

    @Autowired
    private LikesRepository likesRepository;

    public Likes likePost(Likes like) {
        // Only save if not already liked by this user on this post
        if (!likesRepository.existsByPostAndUser(
                like.getPost(),
                like.getUser()
        )) {
            return likesRepository.save(like);
        }

        // Already liked — return the existing "like" object (or you could return null)
        return like;
    }

    public List<Likes> getLikesByPost(Long postId) {
        return likesRepository.findByPostId(postId);
    }

    public void unlikePost(Long id) {
        likesRepository.deleteById(id);
    }

    public Likes findByPostAndUser(Long postId, Long userId) {
        return likesRepository.findByPostIdAndUserId(postId, userId);
    }

}
