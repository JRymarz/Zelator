package org.zelator.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MysteryChangeRequest {


    @NotNull
    private Long groupId;

    @NotNull
    private Long intentionId;

    @NotNull
    private LocalDateTime eventDate;

    private boolean autoAssign;

    private Map<Long, Long> memberMysteries;

}
