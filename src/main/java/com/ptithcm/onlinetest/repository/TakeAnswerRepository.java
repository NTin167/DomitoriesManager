package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.model.TakeAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeAnswerRepository extends JpaRepository<TakeAnswer, Long> {
    Iterable<TakeAnswer> findAllByTakeId(Long id);
}
