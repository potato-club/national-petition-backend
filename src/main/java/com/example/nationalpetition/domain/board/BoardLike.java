package com.example.nationalpetition.domain.board;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"boardId","memberId"}
                )
        }
)
public class BoardLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardState boardState;

    public BoardLike(Long boardId, Long memberId, BoardState boardState) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardState = boardState;
    }

    public static BoardLike of(Long boardId, Long memberId, BoardState boardState) {
        return new BoardLike(boardId, memberId, boardState);
    }

    public void updateState(BoardState boardState) {
        this.boardState = boardState;
    }

}
