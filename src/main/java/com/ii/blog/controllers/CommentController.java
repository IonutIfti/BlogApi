package com.ii.blog.controllers;
import com.ii.blog.payload.CommentDTO;
import com.ii.blog.services.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/posts/{postId}/newComment")
    public ResponseEntity<CommentDTO> createComment( @PathVariable(name = "postId") Long postId,
                                                     @Valid @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments(@PathVariable(name = "postId") Long postId) {
        return new ResponseEntity<>(commentService.getAllCommentsByPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(name = "postId") Long postId,
                                                     @PathVariable(name = "commentId") Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentById(@PathVariable(name = "postId") Long postId,
                                                        @PathVariable(name = "commentId") Long commentId,
                                                        @Valid @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.updateCommentById(postId, commentId, commentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable(name="postId") Long postId,
                                                        @PathVariable(name="commentId") Long commentId){
        commentService.deleteCommentById(postId,commentId);
        return new ResponseEntity<>("Comment was deleted successfully", HttpStatus.OK);
    }
}
