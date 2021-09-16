package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
import com.example.nationalpetition.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/api/v1/board")
    public ApiResponse<BoardInfoResponse> createBoard(@RequestBody @Valid CreateBoardRequest request) {
        return ApiResponse.success(boardService.createBoard(request));
    }



}
