package com.ptithcm.onlinetest.payload.dto;

import com.ptithcm.onlinetest.model.QuizAnswer;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class QuizQuestionDto {
    private Long id;

    private String type;

    private int active;

    private int level;

    private int score;

    private String content;

    private Set<QuizAnswer> quizAnswers = new HashSet<>();
}
