package me.white.restapi.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
//@RequestMapping(value = "/api/events/", produces = MediaTypes.HAL_JSON_VALUE)// 이렇게 작성하면 모든 응답을 해당 형태로 모내게됨
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/api/events/")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
//        URI createUri = linkTo(methodOn(EventController.class).createEvent(event)).slash("{id}").toUri();
        event.setId(10);
        return ResponseEntity.created(createUri).body(event);
    }
}
