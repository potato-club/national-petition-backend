package com.example.nationalpetition.dto.alarm;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlarmListResponse {

    List<BoardAlarmResponse> alarmList;

    public AlarmListResponse(List<BoardAlarmResponse> alarmList) {
        this.alarmList = alarmList;
    }

}
