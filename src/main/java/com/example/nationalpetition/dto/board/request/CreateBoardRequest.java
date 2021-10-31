package com.example.nationalpetition.dto.board.request;

import com.example.nationalpetition.config.validator.PetitionUrl;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {

    private String title;

    private String content;

    @PetitionUrl
    private String petitionUrl;

    public CreateBoardRequest(String title, String content, String petitionUrl) {
        this.title = title;
        this.content = content;
        this.petitionUrl = petitionUrl;
    }

    public static CreateBoardRequest testInstance(String title, String content, String petitionUrl) {
        return new CreateBoardRequest(title, content, petitionUrl);
    }

    public Board toEntity(PetitionResponse petitionInfo, Long memberId) {
        if(petitionInfo.getCategory() == null) {
            petitionInfo.setCategory(BoardCategory.ETC.getCategory());
        }
        return Board.builder()
                .petitionTitle(petitionInfo.getTitle())
                .petitionContent(petitionInfo.getContent())
                .petitionsCount(petitionInfo.getPetitionsCount())
                .memberId(memberId)
                .category(BoardCategory.getEnglishCategory(petitionInfo.getCategory()))
                .petitionCreatedAt(petitionInfo.getCreatedAt())
                .petitionFinishedAt(petitionInfo.getFinishedAt())
                .petitionUrl(petitionUrl)
                .title(title)
                .content(content)
                .build();
    }

}
