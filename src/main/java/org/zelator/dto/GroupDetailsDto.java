package org.zelator.dto;


import lombok.Data;
import org.zelator.entity.Intention;

import java.util.List;

@Data
public class GroupDetailsDto {


    private Long id;
    private String name;
    private Intention intention;

    private MemberDetailsDto leader;
    private List<MemberDetailsDto> members;


    @Data
    public static class MemberDetailsDto{
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private MysteryDto mystery;
    }

}
