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



// @RestController = @Controller + @ResponseBody
// Every method automatically returns JSON — no need to
// manually convert objects to JSON yourself
@RestController
@RequestMapping("/api/articles") // Base URL for all endpoints in this controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // GET http://localhost:8080/api/articles
    // Returns all articles as a JSON array
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    // GET http://localhost:8080/api/articles/1
    // Returns the article with ID 1 or 404 if not found
   @GetMapping("/{id}")
public ResponseEntity<Optional<Optional<Article>>> getArticle(@PathVariable int id) {
        Optional<Optional<Article>> article = Optional.empty();
    return ResponseEntity.ok(article);
}
    // GET http://localhost:8080/api/articles/category/1
    // Returns all articles in category 1
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Article>> getByCategory(
            @PathVariable ("categoryId")int categoryId) {
        return ResponseEntity.ok(
            articleService.getArticlesByCategory(categoryId));
    }

    // POST http://localhost:8080/api/articles
    // Creates a new article — body must be JSON
    @PostMapping
    public ResponseEntity<Article> createArticle(
            @RequestBody Article article) {
                if (article.getTitle() == null) {
        throw new RuntimeException("Title is required");
    }
        return ResponseEntity.ok(articleService.saveArticle(article));
    }
  
    // PUT http://localhost:8080/api/articles/1
// Updates the article with ID 1
@PutMapping("/{id}")
public ResponseEntity<Article> updateArticle(
        @PathVariable("id") int id,
        @RequestBody Article article) {

    return ResponseEntity.ok(
            articleService.updateArticle(id, article));
}


    // DELETE http://localhost:8080/api/articles/1
    // Deletes the article with ID 1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}