package com.example.sns.controller.response;

import com.example.sns.model.AlarmArgument;
import com.example.sns.model.AlarmType;
import com.example.sns.model.User;
import com.example.sns.model.entity.Alarm;
import com.example.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class AlarmResponse {

    private Integer id;
    private AlarmType alarmType;
    private AlarmArgument alarmArgument;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromEntity(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getAlarmArgument(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
