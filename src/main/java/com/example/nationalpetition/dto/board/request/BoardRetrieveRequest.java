package com.example.nationalpetition.dto.board.request;

import com.example.nationalpetition.domain.board.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BoardRetrieveRequest {

    @NotNull
    private String search;
    @Min(1)
    private int size;
    private String sort;
    @Min(1)
    private int page;

    private BoardCategory category;

}
