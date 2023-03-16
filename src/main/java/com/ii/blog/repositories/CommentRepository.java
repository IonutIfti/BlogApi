package com.ii.blog.repositories;

import com.ii.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    Optional<Comment> findById(Long commentId);
    @Query("SELECT c FROM Comment c JOIN c.post p WHERE p.id = :postId AND c.id = :commentId")
    Comment findByPostIdCommentId(Long postId, Long commentId);

}
