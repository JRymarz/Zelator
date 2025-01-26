package org.zelator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CalendarEventDto {


    private Long id;
    private String title;
    private LocalDateTime eventDate;
    private String eventType;
    private String creatorName;

}
