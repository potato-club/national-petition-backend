package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comment/{boardId}")
    public ApiResponse<Long> addComment(@RequestBody CommentCreateDto dto,
                                        @PathVariable Long boardId) {
        return ApiResponse.success(commentService.addComment(dto, boardId));
    }

    @PutMapping("/api/v1/comment")
    public ApiResponse<Comment> updateComment(@RequestBody CommentUpdateDto dto) {
        return ApiResponse.success(commentService.updateComment(dto));
    }



}
