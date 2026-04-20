package com.techpulse.repository;

import java.util.List;

//import javax.swing.Spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.techpulse.model.Article;
import com.techpulse.model.Article.Status;



    @Repository
    public interface ArticleRepository extends JpaRepository<Article , Integer>{
        
        // Spring Data JPA reads this method name and automatically
    // generates the correct SQL — SELECT * FROM articles
    // WHERE category_id = ?
        List<Article> findByCategoryId(int categoryId);
        List<Article> findByStatus(Status  status);

    public boolean existsByUrl(String url);
          }
    

