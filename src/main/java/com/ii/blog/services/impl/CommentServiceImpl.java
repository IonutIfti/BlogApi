package com.ii.blog.services.impl;
import com.ii.blog.entities.Comment;
import com.ii.blog.entities.Post;
import com.ii.blog.exceptions.BlogApiException;
import com.ii.blog.exceptions.ResourceNotFound;
import com.ii.blog.mappers.CommentMapper;
import com.ii.blog.payload.CommentDTO;
import com.ii.blog.repositories.CommentRepository;
import com.ii.blog.repositories.PostRepository;
import com.ii.blog.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = commentMapper.mapToEntity(commentDTO);
        Post post = postRepository.findById(postId).orElseThrow(()-> new
                ResourceNotFound("Post", "id",postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return commentMapper.mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> getAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(commentMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        Comment comment = findCommentById(postId, commentId);
        return commentMapper.mapToDTO(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDTO) {
        Comment comment = findCommentById(postId, commentId);
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.mapToDTO(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Comment existingComment = findCommentById(postId, commentId);
        commentRepository.delete(existingComment);
    }
    private Comment findCommentById(Long postId, Long commentId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new
                ResourceNotFound("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new
                ResourceNotFound("Comment","id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return comment;
    }
}

