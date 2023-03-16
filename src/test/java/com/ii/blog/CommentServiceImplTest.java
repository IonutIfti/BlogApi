package com.ii.blog;

import com.ii.blog.entities.Comment;
import com.ii.blog.entities.Post;
import com.ii.blog.mappers.CommentMapper;
import com.ii.blog.payload.CommentDTO;
import com.ii.blog.repositories.CommentRepository;
import com.ii.blog.repositories.PostRepository;
import com.ii.blog.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceImplTest {
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository, postRepository, commentMapper);
    }

    @Test
    public void createComment() {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName("John Test");
        commentDTO.setEmail("john@gmail.com");
        commentDTO.setBody("Testing John comment");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("John Test");
        comment.setEmail("john@gmail.com");
        comment.setBody("Testing John comment");
        Post post = new Post();
        post.setId(1L);
        post.setComments(new HashSet<>());
        comment.setPost(post);
        when(commentMapper.mapToEntity(commentDTO)).thenReturn(comment);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.mapToDTO(comment)).thenReturn(commentDTO);


        CommentDTO createdComment = commentService.createComment(1L, commentDTO);


        assertEquals(createdComment.getName(), commentDTO.getName());
        assertEquals(createdComment.getEmail(), commentDTO.getEmail());
        assertEquals(createdComment.getBody(), commentDTO.getBody());
    }

    @Test
    public void getAllCommentsByPostId() {

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("Jim Test");
        comment.setEmail("jim@gmail.com");
        comment.setBody("Testing jim Comment");
        Post post = new Post();
        post.setId(1L);
        post.setComments(new HashSet<>());
        comment.setPost(post);
        comments.add(comment);
        when(commentRepository.findByPostId(anyLong())).thenReturn(comments);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setName("Jim Test");
        commentDTO.setEmail("jim@gmail.com");
        commentDTO.setBody("Testing jim Comment");
        when(commentMapper.mapToDTO(comment)).thenReturn(commentDTO);


        List<CommentDTO> allComments = commentService.getAllCommentsByPostId(1L);


        assertEquals(allComments.size(), 1);
        assertEquals(allComments.get(0).getName(), commentDTO.getName());
        assertEquals(allComments.get(0).getEmail(), commentDTO.getEmail());
        assertEquals(allComments.get(0).getBody(), commentDTO.getBody());
    }
}