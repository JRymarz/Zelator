package org.zelator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberStatusResponse {


    private Long id;
    private String firstName;
    private String lastName;
    private Boolean status;

}
