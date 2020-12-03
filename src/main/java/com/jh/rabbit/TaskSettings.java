package com.jh.rabbit;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskSettings {
    private UUID id;
    private UUID userId;

    private Integer timelineTaskDuration;
    private AutomaticScheduling automaticScheduling;

}
