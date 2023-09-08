package me.white.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.white.restapi.common.RestDocsConfiguration;
import me.white.restapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest //
@SpringBootTest // 이 어노테이션은 스프링부트어플리케이션을 찾아서 거기서부터 빈등록을 하면서 진행->
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    // mock 웹서버를 띄우지 않기 때문에 웹서버보다는 빠르지만 단위테스트보다 빠르지는 않음

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-event").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(linkWithRel("self").description("link to self "),
                                linkWithRel("query-event").description("link to query-event "),
                                linkWithRel("update-event").description("link to update-event "))
                ,requestHeaders( // 헤더 문서화
                        headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        )
                ,requestFields( // 필드 문서화
                        fieldWithPath("name").description("Name of New event"),
                        fieldWithPath("description").description("Description of New event"),
                        fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of New event"),
                        fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                        fieldWithPath("beginEventDateTime").description("beginEventDateTime of New event"),
                        fieldWithPath("endEventDateTime").description("endEventDateTime of New event"),
                        fieldWithPath("basePrice").description("basePrice of New event"),
                        fieldWithPath("maxPrice").description("maxPrice of New event"),
                        fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of New event"),
                        fieldWithPath("location").description("location of New event")
                )
                ,responseHeaders( // 헤더 문서화
                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                )
                ,responseFields( // relaxed를 붙이면 문서의 일부분만 있어도 된다는 메소드 -links 무시- 단점은 정확한 문서를 만들수 없다는것
                        fieldWithPath("id").description("id of New event"),
                        fieldWithPath("name").description("name of New event"),
                        fieldWithPath("description").description("description of New event"),
                        fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of New event"),
                        fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                        fieldWithPath("beginEventDateTime").description("beginEventDateTime of New event"),
                        fieldWithPath("endEventDateTime").description("endEventDateTime of New event"),
                        fieldWithPath("basePrice").description("basePrice of New event"),
                        fieldWithPath("maxPrice").description("maxPrice of New event"),
                        fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of New event"),
                        fieldWithPath("location").description("location of New event"),
                        fieldWithPath("free").description("free of New event"),
                        fieldWithPath("offline").description("it tells if this event is offline"),
                        fieldWithPath("eventStatus").description("eventStatus"),
                        fieldWithPath("_links.self.href").description("links to self"),
                        fieldWithPath("_links.query-event.href").description("links to query-event"),
                        fieldWithPath("_links.update-event.href").description("links to update-event")
                )
                ))
        ;

    }

    @Test
    @TestDescription("입력받을수 없는 값을 입력받은경우 에러가 발생하는 테스트")
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
    @TestDescription("입력값이 비어있는경우 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @Description("입력값이 잘못된경우 에러가 발생하는 테스트")
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
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists()) // []는 인덱스,글로벌 에러의 경우 필드값과 리젝티드벨류값이 없으므로 에러날 수 있음그래서 지움
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }

}
