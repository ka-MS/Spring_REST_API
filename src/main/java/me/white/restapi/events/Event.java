package me.white.restapi.events;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter // data 애노테이션의 경우 엔티티에 사용하면 상호참조때문에 스택오버플로우 발생할 수 있음
@Getter
@EqualsAndHashCode(of = {"id"}) // 해시코드간의 비교
public class Event {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline; // (optional)
    private boolean free;
    private EventStatus eventStatus;

}
