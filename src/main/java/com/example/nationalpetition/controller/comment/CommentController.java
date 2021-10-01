package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.config.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentPageResponseDto;
import com.example.nationalpetition.dto.comment.response.CommentRetrieveResponseDto;
import com.example.nationalpetition.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comment/{boardId}")
    public ApiResponse<Long> addComment(@RequestBody CommentCreateDto dto,
                                        @PathVariable Long boardId,
                                        @MemberId Long memberId) {
        return ApiResponse.success(commentService.addComment(dto, boardId, memberId));
    }

    @PutMapping("/api/v1/comment")
    public ApiResponse<String> updateComment(@MemberId Long memberId, @RequestBody CommentUpdateDto dto) {
        return ApiResponse.success(commentService.updateComment(memberId, dto).getContent());
    }

    @DeleteMapping("/api/v1/comment/{commentId}")
    public ApiResponse<String> deleteComment(@MemberId Long memberId, @PathVariable Long commentId) {
        commentService.deleteComment(memberId, commentId);
        return ApiResponse.OK;
    }

    @GetMapping("/api/v1/comment/{boardId}")
    public ApiResponse<List<CommentRetrieveResponseDto>> retrieveComments(@PathVariable Long boardId) {
        return ApiResponse.success(commentService.retrieveComments(boardId));
    }

    @PostMapping("/api/v1/comment/like")
    public ApiResponse<String> likeComment(@MemberId Long memberId, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        commentService.addStatus(memberId, likeCommentRequestDto);
        return ApiResponse.OK;
    }

    @DeleteMapping("/api/v1/comment/unlike")
    public ApiResponse<String> deleteStatus(@MemberId Long memberId, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        commentService.deleteStatus(memberId, likeCommentRequestDto);
        return ApiResponse.OK;
    }

    @GetMapping("/api/v1/comment/page")
    public ApiResponse<CommentPageResponseDto> commentPage(@RequestParam int page,
                                                           @RequestParam int size) {
        return ApiResponse.success(commentService.pageRequest(page, size));
    }


}
