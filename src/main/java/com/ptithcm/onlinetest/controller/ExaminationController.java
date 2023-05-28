package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.payload.dto.QuestionTakeDto;
import com.ptithcm.onlinetest.security.CurrentUser;
import com.ptithcm.onlinetest.security.UserPrincipal;
import com.ptithcm.onlinetest.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examination")
public class ExaminationController {

    @Autowired
    QuizService quizService;

    @PostMapping("/submit/{quizId}")
    public ResponseEntity<?> submitQuiz(@PathVariable(value = "quizId") Long quizId,@RequestBody List<Long> listAnswersId) {
//        Long quizId = Long.valueOf(1);
//        Long [] arr = {Long.valueOf(1), Long.valueOf(3), Long.valueOf(4), Long.valueOf(1), Long.valueOf(7), Long.valueOf(8), Long.valueOf(9)};
//        List<Long> answerIds = Arrays.asList(arr);

        return quizService.submitQuiz(quizId, listAnswersId);
    }

    @PostMapping("/take/{quizId}")
    public QuestionTakeDto takeQuiz(@CurrentUser UserPrincipal userPrincipal, @PathVariable("quizId") Long quizId)
    {
        return quizService.takeQuestion(userPrincipal, quizId);
    }

}
