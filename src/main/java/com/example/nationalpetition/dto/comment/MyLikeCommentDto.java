package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.LikeComment;
import com.example.nationalpetition.domain.comment.LikeCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyLikeCommentDto {

    private Long memberId;

    private LikeCommentStatus myStatus;

    @Builder
    public MyLikeCommentDto(Long memberId, LikeCommentStatus myStatus) {
        this.memberId = memberId;
        this.myStatus = myStatus;
    }

    public static MyLikeCommentDto of(LikeComment likeComment) {
        return MyLikeCommentDto.builder()
                .memberId(likeComment.getMemberId())
                .myStatus(likeComment.getLikeCommentStatus())
                .build();
    }

}
