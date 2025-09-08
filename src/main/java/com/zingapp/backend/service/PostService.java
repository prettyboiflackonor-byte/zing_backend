package com.zingapp.backend.service;

import com.zingapp.backend.dto.PostDTO;
import com.zingapp.backend.dto.PostResponseDTO;
import com.zingapp.backend.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(PostDTO dto);
    List<PostResponseDTO> getAllPosts();
    List<PostResponseDTO> getPostsByUserID(Long userID);

    void deletePost(Long postId, Long userId);
}
