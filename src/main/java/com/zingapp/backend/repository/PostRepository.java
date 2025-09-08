package com.zingapp.backend.repository;

import com.zingapp.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Hent alle poster fra en spesifikk bruker
    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    List<Post> findAllOrderedByDateDesc();

    //sorterer postene fra en bruker
    List<Post> findByUserIdOrderByDateDesc(Long userId);

}



