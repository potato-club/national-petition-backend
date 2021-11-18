package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.Comment;
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

    @Builder
    private LikeCommentDto(long likeCounts, long unLikeCounts) {
        this.likeCounts = likeCounts;
        this.unLikeCounts = unLikeCounts;
    }

    public static LikeCommentDto of(List<LikeComment> likeComments) {
        long likeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.LIKE))
                .count();

        long unLikeCounts = likeComments.stream()
                .filter(likeComment -> likeComment.getLikeCommentStatus().equals(LikeCommentStatus.UNLIKE))
                .count();

        return new LikeCommentDto(likeCounts, unLikeCounts);

    }

}
