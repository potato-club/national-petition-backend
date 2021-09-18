package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.CommentCreateDto;
import com.example.nationalpetition.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId) {
        if (dto.getParentId() == null) {
            return commentRepository.save(Comment.newRootComment(dto.getMemberId(), boardId, dto.getContent())).getId();
        }
        Comment parentComment = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new NotFoundException(String.format("(%s)에 해당하는 댓글이 존재하지 않아요.", dto.getParentId())));
        int depth = parentComment.getDepth();
        return commentRepository.save(Comment.newChildComment(dto.getParentId(), dto.getMemberId(), boardId, depth + 1, dto.getContent())).getId();
    }

}