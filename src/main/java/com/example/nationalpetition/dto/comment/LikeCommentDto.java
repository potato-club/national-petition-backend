package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.LikeComment;
import com.example.nationalpetition.domain.comment.LikeCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LikeCommentDto {

    private long likeCounts;

    private long unLikeCounts;

    private List<MyLikeCommentDto> myStatus;

    @Builder
    private LikeCommentDto(long likeCounts, long unLikeCounts, List<MyLikeCommentDto> myStatus) {
        this.likeCounts = likeCounts;
        this.unLikeCounts = unLikeCounts;
        this.myStatus = myStatus;
    }

    public static LikeCommentDto of(List<LikeComment> likeComments, Long memberId) {
        long likeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.LIKE))
                .count();

        long unLikeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.UNLIKE))
                .count();

        List<MyLikeCommentDto> myDto = likeComments
                .stream().map(MyLikeCommentDto::of)
                .filter(likeComment -> likeComment.getMemberId().equals(memberId))
                .collect(Collectors.toList());


        return new LikeCommentDto(likeCounts, unLikeCounts, myDto);

    }

}
