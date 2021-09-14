package com.example.nationalpetition.dto.board.request;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {

    private Long petitionId;

    private String title;

    private String content;

    private Long memberId;

    public CreateBoardRequest(Long petitionId, String title, String content, Long memberId) {
        this.petitionId = petitionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public static CreateBoardRequest testInstance(Long petitionId, String title, String content, Long memberId) {
        return new CreateBoardRequest(petitionId, title, content, memberId);
    }

    public Board toEntity(PetitionResponse petitionInfo) {
        return Board.builder()
                .petitionTitle(petitionInfo.getTitle())
                .petitionContent(petitionInfo.getContent())
                .petitionsCount(petitionInfo.getPetitionsCount())
                .memberId(memberId)
                .title(title)
                .content(content)
                .build();
    }

}
