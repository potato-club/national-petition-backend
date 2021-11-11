package com.example.nationalpetition.dto.alarm;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardAlarmResponse {

    private long boardId;
    private String boardTitle;
    private int alarmCount;
}
