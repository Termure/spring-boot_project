package com.workspace.myapplication.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workspace.myapplication.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  List<Tutorial> findByPublished(boolean published);
  List<Tutorial> findByTitleContaining(String title);

  // define custom query using JPQL with index params
  @Query("select t from Tutorial t where t.title = ?1 and t.published = ?2")
  Tutorial findByJPQL(String title, Boolean published);

  // define custom query using JPQL with named params
  @Query("select t from Tutorial t where t.title =:title and t.published =:published")
  Tutorial findByJPQLNamedParams(@Param("title") String title, @Param("published") Boolean published);
}
