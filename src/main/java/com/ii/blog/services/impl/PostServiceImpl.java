package com.ii.blog.services.impl;

import com.ii.blog.entities.Post;
import com.ii.blog.exceptions.ResourceNotFound;
import com.ii.blog.mappers.PostMapper;
import com.ii.blog.payload.PostDTO;
import com.ii.blog.payload.PostResponse;
import com.ii.blog.repositories.CommentRepository;
import com.ii.blog.repositories.PostRepository;
import com.ii.blog.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        try {
            Post newPost = postRepository.save(postMapper.mapToEntity(postDTO));
            log.info("New Post with ID: {}, was created", newPost.getId());
            return postMapper.mapToDTO(newPost);
        } catch (Exception e) {
            log.error("Invalid input {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize, String sortBy) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Post> posts = postRepository.findAll(pageable);
            List<Post> lisOfPosts = posts.getContent();
            List<PostDTO> content =  lisOfPosts.stream().map(postMapper::mapToDTO).collect(Collectors.toList());
            PostResponse postResponse = new PostResponse();
            postResponse.setContent(content);
            postResponse.setPageNo(posts.getNumber());
            postResponse.setPageSize(posts.getSize());
            postResponse.setTotalElements(posts.getTotalElements());
            postResponse.setTotalPages(posts.getTotalPages());
            postResponse.setLast(posts.isLast());
            log.info("Getting all {} posts",postResponse.getTotalElements());
            return postResponse;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }



    @Override
    public PostDTO getPostById(Long id){
        try {
            Post post = postRepository.findById(id).orElseThrow(()-> new
                    ResourceNotFound("Post", "id",id));
            log.info("Found post with ID: {}",id);
            return postMapper.mapToDTO(post);
        }
        catch (ResourceNotFound e) {
            log.error("Didn`t find post with ID: {}",id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id){
        try {
            log.info("Updated post with ID: {}",id);
            Post post = postRepository.findById(id).orElseThrow(()-> new
                    ResourceNotFound("Post", "id",id));
            post.setTitle(postDTO.getTitle());
            post.setDescription(postDTO.getDescription());
            post.setContent(postDTO.getContent());
            Post updatedPost = postRepository.save(post);
            return postMapper.mapToDTO(updatedPost);
        } catch (ResourceNotFound e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePostById(Long id) {

        try {
            Post post = postRepository.findById(id).orElseThrow(()-> new
                    ResourceNotFound("Post", "id",id));
            postRepository.deleteById(post.getId());
            log.info("Deleted post with ID: {}", id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
