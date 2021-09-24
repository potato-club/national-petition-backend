package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentDeleteDto;
import com.example.nationalpetition.dto.comment.request.CommentRetrieveRequestDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.response.CommentRetrieveResponseDto;
import com.example.nationalpetition.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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
    public ApiResponse<String> updateComment(@RequestBody CommentUpdateDto dto) {
        return ApiResponse.success(commentService.updateComment(dto).getContent());
    }

    @DeleteMapping("/api/v1/comment")
    public ApiResponse<CommentRetrieveResponseDto> deleteComment(@RequestBody CommentDeleteDto deleteDto) {
        return ApiResponse.success(CommentRetrieveResponseDto.of(commentService.deleteComment(deleteDto)));
    }

    @GetMapping
    public ApiResponse<List<CommentRetrieveResponseDto>> retrieveComments(@RequestBody CommentRetrieveRequestDto requestDto) {
        return ApiResponse.success(commentService.retrieveComments(requestDto));

    }


}
