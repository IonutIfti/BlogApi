package com.ii.blog.payload;

import com.ii.blog.entities.Comment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    @NotEmpty(message = "Title should not be empty or null")
    @Size(min=2, message = "Title should have at least 2 characters")
    private String title;
    @NotEmpty(message = "Description should not be empty or null")
    @Size(min=15, message = "Description should have at least 15 characters")
    private String description;
    @NotEmpty(message = "Content should not be empty or null")
    private String content;
    private Set<CommentDTO> comments;

}
