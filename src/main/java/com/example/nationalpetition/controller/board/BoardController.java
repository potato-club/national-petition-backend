package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.board.request.*;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseWithLikeCount;
import com.example.nationalpetition.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

	@Operation(summary = "게시글 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board")
	public ApiResponse<BoardInfoResponseWithLikeCount> createBoard(@RequestBody @Valid CreateBoardRequest request, @MemberId Long memberId) {
		return ApiResponse.success(boardService.createBoard(request, memberId));
	}

	@Operation(summary = "게시글 수정하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board/update")
	public ApiResponse<BoardInfoResponseWithLikeCount> updateBoard(@RequestBody @Valid UpdateBoardRequest request, @MemberId Long memberId) {
		return ApiResponse.success(boardService.updateBoard(request, memberId));
	}

	@Operation(summary = "특정 게시글 불러오는 API")
	@GetMapping("/api/v1/getBoard/{boardId}")
	public ApiResponse<BoardInfoResponseWithLikeCount> getBoard(@PathVariable Long boardId) {
		return ApiResponse.success(boardService.getBoard(boardId));
	}

	@Operation(summary = "게시글 리스트 불러오는 API")
	@GetMapping("/api/v1/getBoard/list")
	public ApiResponse<List<BoardInfoResponseWithLikeCount>> retrieveBoard(@Valid BoardRetrieveRequest request) {
		String sort = request.getSort() == null ? "id" :  request.getSort();
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(DESC, sort));
		return ApiResponse.success(boardService.retrieveBoard(request.getSearch(), pageable));
	}

	@Operation(summary = "게시글 찬성 / 반대하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/board/like")
	public ApiResponse<String> boardLikeOrUnLike(@RequestBody @Valid BoardLikeRequest request, @MemberId Long memberId) {
		boardService.boardLikeOrUnLike(request, memberId);
		return ApiResponse.OK;
	}

	@Operation(summary = "게시글 찬성 / 반대 한것을 삭제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@DeleteMapping("/api/v1/board/like")
	public ApiResponse<String> deleteBoardLikeOrUnLike(@RequestBody @Valid DeleteBoardLikeRequest request, @MemberId Long memberId) {
		boardService.deleteBoardLikeOrUnLike(request.getBoardId(), memberId);
		return ApiResponse.OK;
	}

}
