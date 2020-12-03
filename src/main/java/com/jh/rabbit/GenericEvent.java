package com.jh.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericEvent {
    UUID id;
    String type;

    UUID userId;

    String subjectType;
    UUID subjectId;
    Object subject;
}
