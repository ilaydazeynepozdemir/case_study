package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.model.EventDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();

    List<EventDTO> getEvents();

    EventDTO getEventById(long eventId);

    EventDTO createEvent(EventDTO eventRequest);
}
