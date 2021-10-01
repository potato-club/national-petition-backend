package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PetitionUrlTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validatorFromFactory;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validatorFromFactory = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void test_should_fail() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www1.president.go.kr/petitions/60042sda");

        // when
        Set<ConstraintViolation<CreateBoardRequest>> violations = validatorFromFactory.validate(request);

        // then
        // 실패하면 [ConstraintViolationImpl{interpolatedMessage='URL 형식이 맞지 않습니다.'...,}]
        assertThat(violations).isNotEmpty();
    }

    @Test
    void test_should_fail2() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www.president.go.kr/petitions/60042");

        // when
        Set<ConstraintViolation<CreateBoardRequest>> violations = validatorFromFactory.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void test_should_fail3() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www.president.go.kr/petition/60042");

        // when
        Set<ConstraintViolation<CreateBoardRequest>> violations = validatorFromFactory.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void test_should_success() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www1.president.go.kr/petitions/60042");

        // when
        Set<ConstraintViolation<CreateBoardRequest>> violations = validatorFromFactory.validate(request);

        // then
        // 성공하면 통과하므로 빈배열이 나옴
        assertThat(violations).isEmpty();
    }

}
