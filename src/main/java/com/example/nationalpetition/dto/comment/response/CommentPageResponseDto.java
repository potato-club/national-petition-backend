package com.example.nationalpetition.dto.comment.response;

import com.example.nationalpetition.dto.comment.CommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
