package ru.practicum.explorewithme.event.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.event.model.StateAction;

@Getter
public class UpdateEventUserRequest {

    @Length(max = 1024)
    private String annotation;
    private Long category;
    @Length(max = 5000)
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantsLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Length(max = 256)
    private String title;
}
