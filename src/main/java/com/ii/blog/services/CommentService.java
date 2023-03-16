package com.ii.blog.services;

import com.ii.blog.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(Long postId, CommentDTO commentDTO);
    List<CommentDTO> getAllCommentsByPostId(Long postId);
    CommentDTO getCommentById(Long postId, Long commentId);
    CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDTO);
    void deleteCommentById(Long postId, Long commentId);
}
