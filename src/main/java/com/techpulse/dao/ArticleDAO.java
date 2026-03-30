
    package com.techpulse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.techpulse.model.Article;
import com.techpulse.util.DBConnection;


public class ArticleDAO {

    // Insert a new article
    public void insertArticle(Article article) {
        String sql = "INSERT INTO articles (title, summary, url, " +
                     "published_at, source_id, category_id, " +
                     "type, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getSummary());
            stmt.setString(3, article.getUrl());
            stmt.setString(4, article.getPublishedAt());
            stmt.setInt(5, article.getSourceId());
            stmt.setInt(6, article.getCategoryId());
            stmt.setString(7, article.getType());
            stmt.setString(8, article.getStatus());
            stmt.executeUpdate();
            System.out.println("Article inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all articles
    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setTitle(rs.getString("title"));
                article.setSummary(rs.getString("summary"));
                article.setUrl(rs.getString("url"));
                article.setPublishedAt(rs.getString("published_at"));
                article.setSourceId(rs.getInt("source_id"));
                article.setCategoryId(rs.getInt("category_id"));
                article.setType(rs.getString("type"));
                article.setStatus(rs.getString("status"));
                articles.add(article);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Retrieve articles by category
    public List<Article> getArticlesByCategory(int categoryId) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setTitle(rs.getString("title"));
                article.setSummary(rs.getString("summary"));
                article.setUrl(rs.getString("url"));
                article.setType(rs.getString("type"));
                article.setStatus(rs.getString("status"));
                articles.add(article);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Delete an article by ID
    public void deleteArticle(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Article deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

