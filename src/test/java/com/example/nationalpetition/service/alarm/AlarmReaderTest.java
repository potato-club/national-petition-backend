package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlarmReaderTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AlarmStore alarmStore;

    @Autowired
    private AlarmValidator alarmValidator;

    @Autowired
    private AlarmRepository alarmRepository;

    @AfterEach
    void clear() {
        boardRepository.deleteAll();
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        alarmRepository.deleteAll();
    }

    @Test
    void getAlarmList() {
        //given

        //when

        //then

    }
}