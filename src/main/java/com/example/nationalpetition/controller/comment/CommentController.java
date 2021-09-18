package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.CommentCreateDto;
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


}
