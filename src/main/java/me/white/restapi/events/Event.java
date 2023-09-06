package me.white.restapi.events;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter // data 애노테이션의 경우 엔티티에 사용하면 상호참조때문에 스택오버플로우 발생할 수 있음
@Getter
@EqualsAndHashCode(of = {"id"}) // 해시코드간 비교
@Entity
@ToString
public class Event {

    @Id
    @GeneratedValue
    @Column
    private Integer id; // 식별자

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
    @Enumerated(EnumType.STRING) // 숫자면 인트 이런식으로 들어갈수가 있어서 스트링으로 지정하는걸 권장
    private EventStatus eventStatus = EventStatus.DRAFT; // (optional)

}
