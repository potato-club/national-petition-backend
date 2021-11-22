package com.example.nationalpetition.dto.alarm;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlarmListResponse {

    List<AlarmResponse> alarmResponseList;

    public AlarmListResponse(List<AlarmResponse> alarmResponseList) {
        this.alarmResponseList = alarmResponseList;
    }

    public static AlarmListResponse of(List<AlarmResponse> alarmResponseList) {
        return new AlarmListResponse(alarmResponseList);
    }
}
