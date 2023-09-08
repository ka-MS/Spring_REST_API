package me.white.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


// RepresentationModel을 사용하면 이벤트 객체 생성해주고 변경해주는 로직 작성해 줘야 함
// bean 직렬화를 사용 하면 event로 감싸넣는다 event:{id:1,name:}.....
// 그게 싫으면 @jasonUnwrapped 사용 
// EntityModel<>을 사용하면 생략해서 작성 가능
// https://docs.spring.io/spring-hateoas/docs/current/reference/html/
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {

        super(event, List.of(links));
//        add(Link.of("http://localhost:8080/api/events/"));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

//    private Event event;
//    public EventResource(Event event) {
//        this.event = event;
//    }
}
