package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.CommentCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId) {
        Comment comment = commentRepository.save(dto.toEntity(boardId));
        return comment.getId();
    }

}
