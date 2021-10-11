package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentPageResponseDto;
import com.example.nationalpetition.dto.comment.response.CommentRetrieveResponseDto;
import com.example.nationalpetition.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/comment/{boardId}")
    public ApiResponse<Long> addComment(@RequestBody @Valid CommentCreateDto dto,
                                        @PathVariable @Valid Long boardId,
                                        @MemberId Long memberId) {
        return ApiResponse.success(commentService.addComment(dto, boardId, memberId));
    }

    @Operation(summary = "댓글 수정하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/api/v1/comment")
    public ApiResponse<String> updateComment(@MemberId Long memberId, @RequestBody @Valid CommentUpdateDto dto) {
        return ApiResponse.success(commentService.updateComment(memberId, dto).getContent());
    }

    @Operation(summary = "댓글 삭제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v1/comment/{commentId}")
    public ApiResponse<String> deleteComment(@MemberId Long memberId, @PathVariable @Valid Long commentId) {
        commentService.deleteComment(memberId, commentId);
        return ApiResponse.OK;
    }

    @Operation(summary = "댓글 조회하는 API")
    @GetMapping("/api/v1/comment/{boardId}")
    public ApiResponse<List<CommentRetrieveResponseDto>> retrieveComments(@PathVariable Long boardId) {
        return ApiResponse.success(commentService.retrieveComments(boardId));
    }

    @Operation(summary = "댓글에 좋아요/싫어요 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/comment/like")
    public ApiResponse<String> likeComment(@MemberId Long memberId, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        commentService.addStatus(memberId, likeCommentRequestDto);
        return ApiResponse.OK;
    }

    @Operation(summary = "댓글에 좋아요/싫어요 제거하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v1/comment/unlike")
    public ApiResponse<String> deleteStatus(@MemberId Long memberId, @RequestBody @Valid LikeCommentRequestDto likeCommentRequestDto) {
        commentService.deleteStatus(memberId, likeCommentRequestDto);
        return ApiResponse.OK;
    }

    @Operation(summary = "댓글 페이지네이션 API")
    @GetMapping("/api/v1/comment/page")
    public ApiResponse<CommentPageResponseDto> commentPage(@RequestParam @Valid int page,
                                                           @RequestParam @Valid int size) {
        return ApiResponse.success(commentService.pageRequest(page, size));
    }

}
