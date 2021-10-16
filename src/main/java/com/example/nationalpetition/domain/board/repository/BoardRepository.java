package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Optional<Board> findByIdAndIsDeletedFalse(Long boardId);

    Page<Board> findByPetitionTitleContainingOrTitleContaining(String search, String search1, Pageable pageable);

    Page<Board> findByMemberIdAndIsDeletedIsFalse(Long memberId, Pageable pageable);

    @Query(value = "select b.* from board b left outer join ("
            + "select count(board_id) as board_count, board_id from board_like group by board_id order by board_count desc ) c on b.id = c.board_id"
            + " where title like CONCAT('%', ?1, '%') or petition_title like CONCAT('%', ?1, '%') order by c.board_count desc",
            countQuery = "select count(*) from board where title like CONCAT('%', ?1, '%') or petition_title like CONCAT('%', ?1, '%')",
            nativeQuery = true
    )
    Page<Board> findBoardPagination(String search, Pageable pageable);

}
