package com.ptithcm.onlinetest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
//@Table(name = "quiz_answer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int active;

    private boolean correct;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quizQuestion", nullable = false)
    @JsonIgnore
    private QuizQuestion quizQuestion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

}
