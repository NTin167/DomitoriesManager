package com.ptithcm.onlinetest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptithcm.onlinetest.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "quiz")
public class Quiz extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String metaTitle;

    private String linkImage;

    private String summary;

    private int type;

    private int score;

    private int published;

    private Instant publishedAt;

    private Instant startsAt;

    private Instant endsAt;

    private String content;


    //host id is foreign key of User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    @JsonIgnore
    private User user;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "catergory_id")
    private Category category;
}
