package com.ii.blog;

import com.ii.blog.entities.Post;
import com.ii.blog.exceptions.ResourceNotFound;
import com.ii.blog.mappers.PostMapper;
import com.ii.blog.payload.PostDTO;
import com.ii.blog.payload.PostResponse;
import com.ii.blog.repositories.CommentRepository;
import com.ii.blog.repositories.PostRepository;
import com.ii.blog.services.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceImplTest {

    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostMapper postMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository, postMapper);
    }

    @Test
    public void createPost() {

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Post Testing");
        postDTO.setDescription("This is a test");
        postDTO.setContent("Testing content");
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Post Testing");
        post.setDescription("This is a test");
        post.setContent("Testing content");
        when(postMapper.mapToEntity(postDTO)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.mapToDTO(post)).thenReturn(postDTO);


        PostDTO createdPost = postService.createPost(postDTO);

        assertEquals(createdPost.getTitle(), postDTO.getTitle());
        assertEquals(createdPost.getDescription(), postDTO.getDescription());
        assertEquals(createdPost.getContent(), postDTO.getContent());
    }

    @Test
    public void getAllPosts() {

        int pageNo = 0;
        int pageSize = 5;
        String sortBy = "title";
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test post");
        post.setDescription("This is a test post");
        post.setContent("Test content");
        posts.add(post);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Post> postPage = new PageImpl<>(posts, pageable, 1);
        when(postRepository.findAll(pageable)).thenReturn(postPage);
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test post");
        postDTO.setDescription("This is a test post");
        postDTO.setContent("Test content");
        List<PostDTO> content = Collections.singletonList(postDTO);
        PostResponse expectedPostResponse = new PostResponse();
        expectedPostResponse.setContent(content);
        expectedPostResponse.setPageNo(postPage.getNumber());
        expectedPostResponse.setPageSize(postPage.getSize());
        expectedPostResponse.setTotalElements(postPage.getTotalElements());
        expectedPostResponse.setTotalPages(postPage.getTotalPages());
        expectedPostResponse.setLast(postPage.isLast());
        when(postMapper.mapToDTO(post)).thenReturn(postDTO);


        PostResponse actualPostResponse = postService.getAllPosts(pageNo, pageSize, sortBy);


        assertEquals(actualPostResponse.getContent().size(), expectedPostResponse.getContent().size());
        assertEquals(actualPostResponse.getPageNo(), expectedPostResponse.getPageNo());
        assertEquals(actualPostResponse.getPageSize(), expectedPostResponse.getPageSize());
        assertEquals(actualPostResponse.getTotalElements(), expectedPostResponse.getTotalElements());
        assertEquals(actualPostResponse.getTotalPages(), expectedPostResponse.getTotalPages());
        assertEquals(actualPostResponse.isLast(), expectedPostResponse.isLast());
        PostDTO actualPostDTO = actualPostResponse.getContent().get(0);
        PostDTO expectedPostDTO = expectedPostResponse.getContent().get(0);
        assertEquals(actualPostDTO.getId(), expectedPostDTO.getId());
        assertEquals(actualPostDTO.getTitle(), expectedPostDTO.getTitle());
        assertEquals(actualPostDTO.getDescription(), expectedPostDTO.getDescription());
        assertEquals(actualPostDTO.getContent(), expectedPostDTO.getContent());
    }

    @Test
    public void getPostById() {

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test post");
        post.setDescription("This is a test post");
        post.setContent("Test content");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test post");
        postDTO.setDescription("This is a test post");
        postDTO.setContent("Test content");
        when(postMapper.mapToDTO(post)).thenReturn(postDTO);


        PostDTO actualPostDTO = postService.getPostById(1L);


        assertEquals(actualPostDTO.getId(), postDTO.getId());
        assertEquals(actualPostDTO.getTitle(), postDTO.getTitle());
        assertEquals(actualPostDTO.getDescription(), postDTO.getDescription());
        assertEquals(actualPostDTO.getContent(), postDTO.getContent());
    }

    @Test
    public void getPostThrowError() {

        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFound thrown = assertThrows(ResourceNotFound.class, () -> postService.getPostById(1L));
        assertEquals(thrown.getResourceName(), "Post");
        assertEquals(thrown.getFieldName(), "id");
        assertEquals(thrown.getFieldValue(), 1L);
    }

    @Test
    public void updatePost() {

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated post");
        postDTO.setDescription("This is an updated post");
        postDTO.setContent("Updated content");
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test post");
        post.setDescription("This is a test post");
        post.setContent("Test content");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.mapToDTO(post)).thenReturn(postDTO);


        PostDTO actualPostDTO = postService.updatePost(postDTO, 1L);


        assertEquals(actualPostDTO.getId(), postDTO.getId());
        assertEquals(actualPostDTO.getTitle(), postDTO.getTitle());
        assertEquals(actualPostDTO.getDescription(), postDTO.getDescription());
        assertEquals(actualPostDTO.getContent(), postDTO.getContent());
    }

    @Test
    public void updatePostThrowError() {

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated post");
        postDTO.setDescription("This is an updated post");
        postDTO.setContent("Updated content");
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFound thrown = assertThrows(ResourceNotFound.class, () -> postService.updatePost(postDTO, 1L));
        assertEquals(thrown.getResourceName(), "Post");
        assertEquals(thrown.getFieldName(), "id");
        assertEquals(thrown.getFieldValue(), 1L);
    }

    @Test
    public void deletePostById() {

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Testing post");
        post.setDescription("Testing the post");
        post.setContent("Test content");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));


        postService.deletePostById(1L);


        verify(postRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deletePostThrowError() {

        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFound thrown = assertThrows(ResourceNotFound.class, () -> postService.deletePostById(1L));
        assertEquals(thrown.getResourceName(), "Post");
        assertEquals(thrown.getFieldName(), "id");
        assertEquals(thrown.getFieldValue(), 1L);
    }


}
