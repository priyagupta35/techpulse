package com.techpulse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpulse.model.Article;
import com.techpulse.repository.ArticleRepository;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByCategory(int categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

    public List<Article> getApprovedArticles() {
        return articleRepository.findByStatus("APPROVED");
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(int id) {
        articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Article not found with id: " + id));
        articleRepository.deleteById(id);
    }
}