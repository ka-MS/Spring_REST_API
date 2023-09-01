package me.white.restapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


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


}