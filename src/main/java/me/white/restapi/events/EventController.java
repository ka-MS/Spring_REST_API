package me.white.restapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
//@RequestMapping(value = "/api/events/", produces = MediaTypes.HAL_JSON_VALUE)// 이렇게 작성하면 모든 응답을 해당 형태로 모내게됨
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/api/events/")
    public ResponseEntity<Event> createEvent(@RequestBody EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        return ResponseEntity.created(createUri).body(event);
    }
}
