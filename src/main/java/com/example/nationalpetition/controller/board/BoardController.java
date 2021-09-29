package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.config.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.DeleteBoardLikeRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseWithLikeCount;
import com.example.nationalpetition.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board")
	public ApiResponse<BoardInfoResponseWithLikeCount> createBoard(@RequestBody @Valid CreateBoardRequest request, @MemberId Long memberId) {
		return ApiResponse.success(boardService.createBoard(request, memberId));
	}

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board/update")
	public ApiResponse<BoardInfoResponseWithLikeCount> updateBoard(@RequestBody @Valid UpdateBoardRequest request, @MemberId Long memberId) {
		return ApiResponse.success(boardService.updateBoard(request, memberId));
	}

	@GetMapping("/api/v1/getBoard/{boardId}")
	public ApiResponse<BoardInfoResponseWithLikeCount> getBoard(@PathVariable Long boardId) {
		return ApiResponse.success(boardService.getBoard(boardId));
	}

	@GetMapping("/api/v1/getBoard/list")
	public ApiResponse<List<BoardInfoResponseWithLikeCount>> retrieveBoard(String search, @PageableDefault(size = 10, sort = "id", direction = DESC) Pageable pageable) {
		return ApiResponse.success(boardService.retrieveBoard(search, pageable));
	}

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board/like")
	public ApiResponse<String> boardLikeOrUnLike(@RequestBody @Valid BoardLikeRequest request, @MemberId Long memberId) {
		boardService.boardLikeOrUnLike(request, memberId);
		return ApiResponse.OK;
	}

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@DeleteMapping("/api/v1/board/like")
	public ApiResponse<String> deleteBoardLikeOrUnLike(@RequestBody @Valid DeleteBoardLikeRequest request, @MemberId Long memberId) {
		boardService.deleteBoardLikeOrUnLike(request.getBoardId(), memberId);
		return ApiResponse.OK;
	}

}
