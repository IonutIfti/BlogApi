package com.ii.blog.mappers;

import com.ii.blog.entities.Comment;
import com.ii.blog.payload.CommentDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {


    CommentDTO mapToDTO(Comment comment);

    Comment mapToEntity(CommentDTO commentDTO);


}
