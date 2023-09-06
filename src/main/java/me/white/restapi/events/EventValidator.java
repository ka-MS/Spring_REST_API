package me.white.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
//            errors.rejectValue("basePrice", "Wrong value", "Base price is wrong.");
//            errors.rejectValue("maxPrice", "Wrong value", "Max price is wrong.");
            errors.reject("wrongPrices","Value for Prices are wrong."); // 글로벌에러로 처리
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "Wrong value", "End event date time is wrong.");
        }// 리젝트 벨류는 필드에러

        // Todo beginEventDateTime
        // Todo closeEnrollmentDateTime
        // Todo beginEnrollmentDateTime
    }
}
