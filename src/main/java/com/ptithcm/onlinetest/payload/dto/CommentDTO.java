package com.ptithcm.onlinetest.payload.dto;


import lombok.*;

import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class CommentDTO {
    private Long id;

    private String text;

    private Long quiz;

    private Long user;
}
