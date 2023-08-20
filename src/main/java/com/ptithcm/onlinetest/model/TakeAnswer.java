package com.ptithcm.onlinetest.model;

import com.ptithcm.onlinetest.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
//@Table(name = "take_answer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAnswer extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int active;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private QuizQuestion quizQuestion;

    @ManyToOne(fetch = FetchType.EAGER)
    private QuizAnswer quizAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Take take;

}
