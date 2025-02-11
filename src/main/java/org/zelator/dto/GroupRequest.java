package org.zelator.dto;


import lombok.Data;

@Data
public class GroupRequest {

    private Long groupId;
    private String name;
    private Long intentionId;
}
