package com.techpulse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpulse.model.Article;
import com.techpulse.model.Article.Status;
import com.techpulse.model.Category;
import com.techpulse.repository.ArticleRepository;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // One single getArticleById method — no duplicates
   public Optional<Article> getArticleById(int id) {
    return articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
}

    public List<Article> getArticlesByCategory(int categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

   public List<Article> getByStatus(String status) {
    return articleRepository.findByStatus(
        Status.valueOf(status.toUpperCase())
    );
}

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
public Article updateArticle(int id, Article updatedArticle) {

    Article existing = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));

    existing.setTitle(updatedArticle.getTitle());

    // only update if not null
    if (updatedArticle.getContent() != null) {
        existing.setContent(updatedArticle.getContent());
    }

    // FIX CATEGORY (important)
    if (updatedArticle.getCategory() != null) {
        Category category = new Category();
        category.setId(updatedArticle.getCategory().getId());
        existing.setCategory(category);
    }

    return articleRepository.save(existing);
}
   

    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }

    public Object getByStatus(String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByStatus'");
    }
}