package com.zingapp.backend.repository;

import com.zingapp.backend.model.Likes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.zingapp.backend.model.Post;
import com.zingapp.backend.model.User;


import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);
    boolean existsByPostAndUser(Post post, User user);

    Likes findByPostIdAndUserId(Long postId, Long userId);

    void deleteByPost_Id(Long postId);




}


