package com.techpulse;

import java.util.List;

import com.techpulse.dao.ArticleDAO;
import com.techpulse.model.Article;

public class App {
    public static void main(String[] args) {

        ArticleDAO articleDAO = new ArticleDAO();

        // First insert a test category and source directly
        // in MySQL before running this, then use their IDs below

        // Test Insert
        Article article = new Article(
            "OpenAI Releases GPT-5",
            "OpenAI has announced the release of GPT-5.",
            "https://techcrunch.com/gpt5",
            "2026-03-28 10:00:00",
            1, 1, "EXTERNAL", "APPROVED"
        );
        articleDAO.insertArticle(article);

        // Test Retrieve All
        List<Article> articles = articleDAO.getAllArticles();
        System.out.println("All Articles:");
        for (Article a : articles) {
            System.out.println(a.getId() + " — " + a.getTitle());
        }

        // Test Retrieve by Category
        List<Article> byCategory = articleDAO.getArticlesByCategory(1);
        System.out.println("Articles in Category 1:");
        for (Article a : byCategory) {
            System.out.println(a.getTitle());
        }

        // Test Delete
        articleDAO.deleteArticle(1);
    }
}
