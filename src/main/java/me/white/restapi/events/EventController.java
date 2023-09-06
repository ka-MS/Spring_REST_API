package me.white.restapi.events;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping("/api/events/")
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors); // 여기서는 .body(errors)로 해서 바디에 값을 넣을 수 없음
            // 왜냐면 자바빈 스펙을 준수한 객체만 제이슨으로 직렬화해서 담을 수 있기 때문
            // 왜 jason으로 변환해야하나 -> 미디어타입을 hal-json으로 지정해놓았기 때문에
        }

        eventValidator.validate(eventDto, errors);
        System.out.println(errors.hashCode());
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        return ResponseEntity.created(createUri).body(event);// 바디에 이벤트 객체를 제이슨으로 담이서 보여줌 
    }
}
