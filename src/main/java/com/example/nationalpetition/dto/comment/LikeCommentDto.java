package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.LikeComment;
import com.example.nationalpetition.domain.comment.LikeCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LikeCommentDto {

    private long likeCounts;

    private long unLikeCounts;

    private LikeCommentStatus myCommentStatus;


    @Builder
    private LikeCommentDto(long likeCounts, long unLikeCounts, LikeCommentStatus myCommentStatus) {
        this.likeCounts = likeCounts;
        this.unLikeCounts = unLikeCounts;
        this.myCommentStatus = myCommentStatus;
    }

    public static LikeCommentDto of(List<LikeComment> likeComments, Long memberId) {
        long likeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.LIKE))
                .count();

        long unLikeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.UNLIKE))
                .count();

        LikeCommentStatus myStatus = likeComments
                .stream()
                .filter(likeComment -> likeComment.getMemberId().equals(memberId))
                .findFirst()
                .map(LikeComment::getLikeCommentStatus)
                .orElse(null);

        return new LikeCommentDto(likeCounts, unLikeCounts, myStatus);
    }

}
