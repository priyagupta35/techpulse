package com.techpulse;

import com.techpulse.dao.ArticleDAO;
import com.techpulse.model.Article;
import com.techpulse.model.Category;
import com.techpulse.model.Source;
import com.techpulse.util.HibernateUtil;

import java.util.List;

public class App {
    public static void main(String[] args) {

        ArticleDAO articleDAO = new ArticleDAO();

        // Fetch existing category and source from DB
        // using their IDs inserted in Phase 1
        Category category = HibernateUtil.getSessionFactory()
            .openSession().get(Category.class, 1);
        Source source = HibernateUtil.getSessionFactory()
            .openSession().get(Source.class, 1);

        // Test Insert
        Article article = new Article();
        article.setTitle("Spring AI Released by VMware");
        article.setSummary("VMware announces Spring AI framework.");
        article.setUrl("https://spring.io/springai");
        article.setPublishedAt(java.time.LocalDateTime.now());
        article.setCategory(category);
        article.setSource(source);
        article.setType("EXTERNAL");
        article.setStatus("APPROVED");
        articleDAO.insertArticle(article);

        // Test Retrieve All
        List<Article> articles = articleDAO.getAllArticles();
        System.out.println("All Articles:");
        for (Article a : articles) {
            System.out.println(a.getId() + " — " + a.getTitle()
                + " | Category: " + a.getCategory().getName());
        }

        // Test Retrieve by Category
        List<Article> byCategory = 
            articleDAO.getArticlesByCategory(1);
        System.out.println("Articles in Category 1:");
        for (Article a : byCategory) {
            System.out.println(a.getTitle());
        }

        // Shutdown Hibernate cleanly
        HibernateUtil.shutdown();
    }
}
