// ArticleController.java
package com.techpulse.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpulse.model.Article;
import com.techpulse.service.ArticleService;
import com.techpulse.service.NewsIngestionService;


// @RestController = @Controller + @ResponseBody
// Every method automatically returns JSON — no need to
// manually convert objects to JSON yourself
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
     @Autowired
    private final ArticleService articleService;
    
    @Autowired
    private final NewsIngestionService newsIngestionService;

    //  Constructor Injection (BEST PRACTICE)
    public ArticleController(ArticleService articleService,
                             NewsIngestionService newsIngestionService) {
        this.articleService = articleService;
        this.newsIngestionService = newsIngestionService;
    }

    // POST /api/articles/fetch
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews() {
        newsIngestionService.fetchAndStoreArticles();
        return ResponseEntity.ok("News ingestion triggered successfully");
    }

    // GET /api/articles
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    //  FIXED (ID endpoint)
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Article> getArticle(@PathVariable int id) {
        Optional<Article> article = articleService.getArticleById(id);

        return article
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/articles/category/1
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Article>> getByCategory(@PathVariable int categoryId) {
        return ResponseEntity.ok(articleService.getArticlesByCategory(categoryId));
    }

    
    // GET /api/articles/status/approved
    @GetMapping("/status/{status}")
    public ResponseEntity<Object> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(articleService.getByStatus(status));
    }

    // POST /api/articles
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        if (article.getTitle() == null) {
            throw new RuntimeException("Title is required");
        }
        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    // PUT /api/articles/1
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable int id,
            @RequestBody Article article) {

        return ResponseEntity.ok(articleService.updateArticle(id, article));
    }

    // DELETE /api/articles/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    public NewsIngestionService getNewsIngestionService() {
        return newsIngestionService;
    }

    public ArticleService getArticleService() {
        return articleService;
    }
}