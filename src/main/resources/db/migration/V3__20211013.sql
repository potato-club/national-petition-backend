ALTER TABLE board
    RENAME COLUMN board_comment_counts TO root_comments_count;

ALTER TABLE comment
    ADD child_comments_count INT;