package com.example.sns.model.entity;

import com.example.sns.model.AlarmArgument;
import com.example.sns.model.AlarmType;
import com.example.sns.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
@AllArgsConstructor
@Getter
public class Alarm {
    private Integer id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgument alarmArgument;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity){
        return new Alarm(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisterAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
