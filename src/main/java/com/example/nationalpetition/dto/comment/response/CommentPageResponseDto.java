package com.example.nationalpetition.dto.comment.response;

import com.example.nationalpetition.dto.comment.CommentDto;
import lombok.Builder;

import java.util.List;

public class CommentPageResponseDto {

    private int totalPages;

    private long totalElements;

    private List<CommentDto> contents;

    @Builder
    public CommentPageResponseDto(int totalPages, long totalElements, List<CommentDto> contents) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.contents = contents;
    }
}
