package com.ptithcm.onlinetest.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "quiz_question")
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private int active;

    private int level;

    private int score;

    private String content;


    @ManyToOne(fetch = FetchType.EAGER)
    private Quiz quiz;

//    @OneToMany(mappedBy = "quizQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToMany( fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "quizQuestion"
    )
    private Set<QuizAnswer> quizAnswers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Set<QuizAnswer> getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(Set<QuizAnswer> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }
}
