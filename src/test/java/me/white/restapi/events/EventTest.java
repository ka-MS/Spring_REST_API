package me.white.restapi.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Infrean Stpring Rest Api")
                .description("REST API development with Spring")
                .build();

        assertThat(event).isNotNull();


    }


    @Test
    public void javaBean() {
        // given
        String name = "Event";
        String description = "Spring";

        // when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // what
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }
    
    
    // 중복코드를 줄여주는 라이브러리 사용 입력값이 없는경우 공란으로 남겨놓으면 된다. ",,true"
    @ParameterizedTest
//    @CsvSource({"0,0,true", "100,0,false", "0,100,false"})
    @MethodSource
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        event.update();

        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] testFree() {
        return new Object[][]{
                {0, 0, true},
                {100, 0, false},
                {0, 100, false},
                {100, 200, false}
        };
    }

    @ParameterizedTest
    @MethodSource
    void testOffline(String location, boolean isOffline) {
        Event event = Event.builder()
                .location(location)
                .build();

        event.update();

        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] testOffline() {
        return new Object[][]{
                {"강남역 사거리", true},
                {null, false},
                {"", false}};
    }
}