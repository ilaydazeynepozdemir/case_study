package com.bilyoner.assignment.couponrest;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.EventDTO;

public class DtoUtils {
    public static EventDTO mapToEventDTO(EventEntity eventEntity) {
        return EventDTO.builder()
                .id(eventEntity.getId())
                .name(eventEntity.getName())
                .mbs(eventEntity.getMbs())
                .type(eventEntity.getType())
                .eventDate(eventEntity.getEventDate())
                .build();
    }
}
