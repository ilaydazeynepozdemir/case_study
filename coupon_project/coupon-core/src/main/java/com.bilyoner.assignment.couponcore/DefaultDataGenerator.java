package com.bilyoner.assignment.couponcore;

import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.service.EventService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class DefaultDataGenerator {
    DefaultDataGenerator(EventService eventService) {
        this.eventService = eventService;
    }

    private final EventService eventService;

    @PostConstruct
    public void init() {
        for (DEFAULT_EVENT defaultEvent : DEFAULT_EVENT.values()) {
            eventService.createEvent(EventDTO.builder()
                    .name(defaultEvent.name())
                    .mbs(defaultEvent.getMbs())
                    .type(defaultEvent.getEventType())
                    .eventDate(LocalDateTime.now())
                    .build());
        }
    }

    private enum DEFAULT_EVENT {
        FOOTBALL_MBS1_1(1, EventTypeEnum.FOOTBALL),
        FOOTBALL_MBS1_2(1, EventTypeEnum.FOOTBALL),
        FOOTBALL_MBS1_3(1, EventTypeEnum.FOOTBALL),

        FOOTBALL_MBS2_1(2, EventTypeEnum.FOOTBALL),
        FOOTBALL_MBS2_2(2, EventTypeEnum.FOOTBALL),
        FOOTBALL_MBS2_3(2, EventTypeEnum.FOOTBALL),
        FOOTBALL_MBS2_4(2, EventTypeEnum.FOOTBALL),

        FOOTBALL_MBS3_1(3, EventTypeEnum.FOOTBALL),

        BASKETBALL_MBS1_1(1, EventTypeEnum.BASKETBALL),

        BASKETBALL_MBS2_1(2, EventTypeEnum.BASKETBALL),
        BASKETBALL_MBS2_2(2, EventTypeEnum.BASKETBALL),
        BASKETBALL_MBS2_3(2, EventTypeEnum.BASKETBALL),

        BASKETBALL_MBS3_1(3, EventTypeEnum.BASKETBALL),

        TENNIS_MBS1_1(1, EventTypeEnum.TENNIS), TENNIS_MBS1_2(1, EventTypeEnum.TENNIS);

        int mbs;
        EventTypeEnum eventType;

        DEFAULT_EVENT(int mbs, EventTypeEnum eventType) {
            this.mbs = mbs;
            this.eventType = eventType;
        }

        public int getMbs() {
            return mbs;
        }

        public EventTypeEnum getEventType() {
            return eventType;
        }
    }
}
