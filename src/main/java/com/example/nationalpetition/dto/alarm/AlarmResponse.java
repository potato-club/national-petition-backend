package com.example.nationalpetition.dto.alarm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmResponse {

    private Long boardId;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;


    public AlarmResponse(Long boardId, String message, LocalDateTime createdDate) {
        this.boardId = boardId;
        this.message = message;
        this.createdDate = createdDate;
    }

    public static AlarmResponse of(Long boardId, String message, LocalDateTime createdDate) {
        return new AlarmResponse(boardId, message, createdDate);
    }


}
