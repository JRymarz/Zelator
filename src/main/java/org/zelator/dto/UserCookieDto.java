package org.zelator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserCookieDto {


    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private GroupDto group;

    @Data
    public static class GroupDto {
        private Long id;
        private String name;
    }

}
