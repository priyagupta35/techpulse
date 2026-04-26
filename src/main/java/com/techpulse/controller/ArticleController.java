package com.techpulse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpulse.model.Article;
import com.techpulse.service.ArticleService;
import com.techpulse.service.NewsIngestionService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NewsIngestionService newsIngestionService;

    // GET /api/articles — returns all articles as JSON array
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    // GET /api/articles/1 — returns single article or 404
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(
            @PathVariable int id) {
        return articleService.getArticleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/articles/approved — public approved articles only
    @GetMapping("/approved")
    public ResponseEntity<List<Article>> getApprovedArticles() {
        return ResponseEntity.ok(articleService.getApprovedArticles());
    }

    // GET /api/articles/category/1 — filter by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Article>> getByCategory(
            @PathVariable int categoryId) {
        return ResponseEntity.ok(
            articleService.getArticlesByCategory(categoryId));
    }

    // POST /api/articles — create new article
    @PostMapping
    public ResponseEntity<Article> createArticle(
            @RequestBody Article article) {
        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    // POST /api/articles/fetch — manually trigger news ingestion
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews() {
        newsIngestionService.fetchAndStoreArticles();
        return ResponseEntity.ok(
            "News ingestion triggered successfully");
    }

    // DELETE /api/articles/1 — delete article by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}