package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    ArrayList<Comment> findAllByQuizId(Long quizId);
}
