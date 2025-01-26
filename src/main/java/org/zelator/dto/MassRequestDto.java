package org.zelator.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MassRequestDto {


    private Long id;

    private AuthorDto user;

    private String intention;

    private LocalDate requestDate;

    private LocalDateTime massDate;

    private String status;


    @Data
    public static class AuthorDto {
        private Long id;

        private String firstName;

        private String lastName;
    }

}
