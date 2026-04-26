package com.techpulse.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techpulse.model.Article;
import com.techpulse.model.Category;
import com.techpulse.model.Source;

@Repository
    public interface CategoryRepository extends JpaRepository<Category,Integer>
    {

    Optional<Article> findById(Long categoryId);

    Optional<Source> findByName(String string);

    }

