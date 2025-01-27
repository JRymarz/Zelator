package org.zelator.dto;


import lombok.Data;

@Data
public class MessageRequest {


    private String message;
    private Long receiverId;
    private Long groupId;

}
