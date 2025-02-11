package org.zelator.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class SimpleGroupDetailsDto {

    private Long id;
    private String name;
    private GroupDetailsDto.MemberDetailsDto leader;
    private List<GroupDetailsDto.MemberDetailsDto> members;
}
