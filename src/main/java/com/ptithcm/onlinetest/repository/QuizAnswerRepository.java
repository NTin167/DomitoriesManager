//package com.ptithcm.onlinetest.repository;
//
//import com.ptithcm.onlinetest.model.QuizAnswer;
//import com.ptithcm.onlinetest.model.QuizQuestion;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Repository
//public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
//    Iterable<QuizAnswer> findQuizAnswerByQuizQuestion(QuizQuestion quizQuestion);
//    Iterable<QuizAnswer> findAllByQuizQuestionId(Long quizQuestionId);
//    @Query("SELECT q.id as id, q.active as active, q.content as content, q.quizQuestion.id as question_id FROM QuizAnswer q WHERE q.correct = 1 AND q.quiz.id = ?1")
//    List<Map<String, Object>> getQuizAnswerTrueByQuizId(Long quizId);
//    Set<QuizAnswer> findAllByQuizId(Long quizId);
//
//    List<QuizAnswer> findByIdIn(List<Long> idS);
//    List<QuizAnswer> findByQuizQuestionIdAndCorrectIsTrue(Long questionId);
//}
