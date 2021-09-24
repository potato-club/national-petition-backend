package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final TokenService tokenService;
    private final BoardService boardService;

    @PostMapping("/api/v1/board")
    public ApiResponse<BoardInfoResponse> createBoard(@RequestBody @Valid CreateBoardRequest request, @RequestHeader("Authorization") String token, Authentication authentication) {
        System.out.println("authentication = " + authentication);
        Long memberId = tokenService.getMemberId(token);
        return ApiResponse.success(boardService.createBoard(request, memberId));
    }

    @PostMapping("/api/v1/board/update")
    public ApiResponse<BoardInfoResponse> updateBoard(@RequestBody @Valid UpdateBoardRequest request, @RequestHeader("Authorization") String token) {
        Long memberId = tokenService.getMemberId(token);
        return ApiResponse.success(boardService.updateBoard(request, memberId));
    }

    @GetMapping("/api/v1/board/{boardId}")
    public ApiResponse<BoardInfoResponse> getBoard(@PathVariable Long boardId) {
        return ApiResponse.success(boardService.getBoard(boardId));
    }

    @GetMapping("/api/v1/board/list")
    public ApiResponse<List<BoardInfoResponse>> retrieveBoard(String search, @PageableDefault(size = 10, sort = "id", direction = DESC) Pageable pageable) {
        return ApiResponse.success(boardService.retrieveBoard(search, pageable));
    }

}
