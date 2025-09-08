package com.zingapp.backend.service;

import com.zingapp.backend.model.Comments;
import com.zingapp.backend.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    public Comments addComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    public List<Comments> getCommentsByPost(Long postId) {
        return commentsRepository.findByPostId(postId);
    }

    public void deleteComment(Long id) {
        commentsRepository.deleteById(id);
    }
}
