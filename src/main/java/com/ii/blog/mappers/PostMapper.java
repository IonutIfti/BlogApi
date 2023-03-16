package com.ii.blog.mappers;

import com.ii.blog.entities.Post;
import com.ii.blog.payload.PostDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface PostMapper {


    PostDTO mapToDTO(Post post);
    Post mapToEntity(PostDTO postDTO);

}

