package com.example.nationalpetition.dto.board.request;

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

    public BoardRetrieveRequest(String search, int size, String sort, int page) {
        this.search = search;
        this.size = size;
        this.sort = sort;
        this.page = page;
    }

    public static BoardRetrieveRequest testInstance(String search, int size, String sort, int page) {
        return new BoardRetrieveRequest(search, size, sort, page);
    }

}
