package com.ii.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    @NotEmpty(message = "Name should not be empty or null")
    @Size(min = 2, message = "Name should be at least 2 characters long")
    private String name;
    @Email(message = "Please enter a valid email address")
    @NotEmpty(message = "Email should not be empty or null")
    private String email;
    @NotEmpty(message = "Body should not be empty or null")
    private String body;
}
