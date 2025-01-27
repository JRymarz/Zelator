package org.zelator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatDto {


    private Long id;
    private String message;
    private LocalDateTime timeStamp;
    private Boolean isRead;
    private String senderName;
    private String groupName;

}
