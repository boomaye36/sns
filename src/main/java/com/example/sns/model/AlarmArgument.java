package com.example.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgument {
    // 알람을 발생시킨 사람
    private Integer fromUserId;
    private Integer targetId;

}
