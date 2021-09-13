package com.example.nationalpetition.controller.comment;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.CommentCreateDto;
import com.example.nationalpetition.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService service;

    @PostMapping("/api/v1/comment/{boardId}")
    public ApiResponse<Long> save(@RequestBody CommentCreateDto dto, @PathVariable Long boardId){
        return ApiResponse.success(service.save(dto, boardId));
    }

}
