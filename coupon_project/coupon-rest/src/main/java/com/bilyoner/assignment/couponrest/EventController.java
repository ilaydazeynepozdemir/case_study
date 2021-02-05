package com.bilyoner.assignment.couponrest;

import com.bilyoner.assignment.couponapi.service.EventService;
import com.bilyoner.assignment.couponapi.model.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    /**
     * TODO : Implement missing parts
     */

    private final EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody @Valid EventDTO eventRequest) {
        return eventService.createEvent(eventRequest);
    }

    /**
     * Implement event endpoints
     */
}
