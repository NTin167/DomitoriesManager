//package com.ptithcm.onlinetest.repository;
//
//import com.ptithcm.onlinetest.model.Quiz;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {
//    boolean existsByTitle(String title);
//    List <Quiz> findAllByCategory_Title(String titleCategory);
//
////    List<Quiz> findAllByCategory_Title(String titleCategopry);
//    Iterable<Quiz> findAllByCategoryId(Long categoryId);
//
//    boolean existsByCategory_Id(Long categoryId);
//
//    Page<Quiz> findAll(Pageable pageable);
//
//    @Query("SELECT q.id, q.title, q.linkImage FROM Quiz q WHERE q.category.id = ?1")
//    Iterable<?> getQuizzesByCategoryId(Long id);
//
//}
