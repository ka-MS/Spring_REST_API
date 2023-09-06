package me.white.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest //
@SpringBootTest // 이 어노테이션은 스프링부트어플리케이션을 찾아서 거기서부터 빈등록을 하면서 진행->
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    // mock 웹서버를 띄우지 않기 때문에 웹서버보다는 빠르지만 단위테스트보다 빠르지는 않음

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    void createEvent() throws Exception {
        // given
        EventDto event = EventDto.builder()
                .name("test")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 1, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 1, 2, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 1, 3, 1, 0))
                .endEventDateTime(LocalDateTime.of(2020, 1, 4, 1, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2 스타텁 팩토리")
                .build();

        // MockBean을 사용하면 강제로 성공상황을 만들어 놓고 테스트를 진행할 수 있다.
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);
//        System.out.println(event);

        // when
        mockMvc.perform(post("/api/events/") // mockmvc 를 이용하면 에러를 던질 수 있음
                .contentType(MediaType.APPLICATION_JSON) // contentType 은 json 응답에 json 을 던지고 있고
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))) // accept header 어떤 답변을 원한다 -> hal_json 형태의 media type 을 원한다. -> 확장자 비슷한 형태
                // then
                .andDo(print())
                .andExpect(status().isCreated()) // 확인할 수 있는 메소드 status 값이 201인지 확인해줘~
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION)) // HttpHeaders 에 location 이 있는지 확인
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    void createEvent_Bad_Request() throws Exception {
        // given
        Event event = Event.builder()
                .id(100)
                .name("test")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 1, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 1, 2, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 1, 3, 1, 0))
                .endEventDateTime(LocalDateTime.of(2020, 1, 4, 1, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .build();


        // when
        mockMvc.perform(post("/api/events/") // mockmvc 를 이용하면 에러를 던질 수 있음
                .contentType(MediaType.APPLICATION_JSON) // contentType 은 json 응답에 json 을 던지고 있고
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))) // accept header 어떤 답변을 원한다 -> hal_json 형태의 media type 을 원한다. -> 확장자 비슷한 형태
        // then
                .andDo(print())
                .andExpect(status().isBadRequest()) // 확인할 수 있는 메소드 status 값이 201인지 확인해줘~
        ;
    }

    @Test
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder() // 날짜라던지 가격등 어노테이션으로 체크하기 힘든경우
                .name("test")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 1, 5, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 1, 4, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 1, 3, 1, 0))
                .endEventDateTime(LocalDateTime.of(2020, 1, 2, 1, 0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

}
