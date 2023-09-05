package me.white.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    // mock 웹서버를 띄우지 않기 때문에 웹서버보다는 빠르지만 단위테스트보다 빠르지는 않음

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        // given
        Event event = Event.builder()
                .name("test")
                .description("Rest API")
                .beginEventDateTime(LocalDateTime.of(2020, 1, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 1, 2, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 1, 3, 1, 0))
                .endEventDateTime(LocalDateTime.of(2020, 1, 4, 1, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2 스타텁 팩토리")
                .build();

        // when
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        // what
        mockMvc.perform(post("/api/events/") // mockmvc를 이용하면 에러를 던질 수 있음
                    .contentType(MediaType.APPLICATION_JSON) // contentType은 json 응답에 json을 던지고 있고
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))) // accept header 어떤 답변을 원한다 -> hal_json형태의 media type을 원한다. -> 확장자 비슷한 형태
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()); // 확인할 수 있는 메소드 status값이 201인지 확인해줘~
    }

}
