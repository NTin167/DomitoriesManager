package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.model.Take;
import com.ptithcm.onlinetest.model.TakeAnswer;
import com.ptithcm.onlinetest.repository.TakeAnswerRepository;
import com.ptithcm.onlinetest.repository.TakeRepository;
import com.ptithcm.onlinetest.repository.UserRepository;
import com.ptithcm.onlinetest.security.CurrentUser;
import com.ptithcm.onlinetest.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
public class HistoryQuiz {

    @Autowired
    TakeRepository takeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TakeAnswerRepository takeAnswerRepository;

    @GetMapping("/getQuizHistory")
   public ResponseEntity<?> getAllQuiz(@CurrentUser UserPrincipal userPrincipal) {
        Iterable<Take> quiz = takeRepository.findAllByUserId(userPrincipal.getId());
        return ResponseEntity.ok().body(quiz);
    }

    @GetMapping("/getDetailQuiz")
    public ResponseEntity<?> getDetailQuiz(@CurrentUser UserPrincipal userPrincipal,
                                           @RequestParam(value = "takeId", required = false) Long takeId) {
        Iterable<TakeAnswer> takeAnswers = takeAnswerRepository.findAllByTakeId(takeId);
        return ResponseEntity.ok().body(takeAnswers);
    }
}
