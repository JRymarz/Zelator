package org.zelator.dto;

import lombok.Data;

@Data
public class UserDto {


    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long groupId;

}
