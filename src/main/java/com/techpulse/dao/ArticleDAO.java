package com.techpulse.dao;

import com.techpulse.model.Article;
import com.techpulse.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ArticleDAO {

    // Insert a new article
    public void insertArticle(Article article) {
        // Open a session from the SessionFactory
        Session session = HibernateUtil.getSessionFactory()
            .openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Hibernate handles the INSERT SQL automatically
            session.persist(article);
            transaction.commit();
            System.out.println("Article inserted successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Retrieve all articles
    public List<Article> getAllArticles() {
        Session session = HibernateUtil.getSessionFactory()
            .openSession();
        try {
            // HQL - Hibernate Query Language, uses class name
            // not table name
            return session.createQuery(
                "FROM Article", Article.class).list();
        } finally {
            session.close();
        }
    }

    // Retrieve articles by category
    public List<Article> getArticlesByCategory(int categoryId) {
        Session session = HibernateUtil.getSessionFactory()
            .openSession();
        try {
            return session.createQuery(
                "FROM Article a WHERE a.category.id = :categoryId",
                Article.class)
                .setParameter("categoryId", categoryId)
                .list();
        } finally {
            session.close();
        }
    }

    // Delete an article by ID
    public void deleteArticle(int id) {
        Session session = HibernateUtil.getSessionFactory()
            .openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Article article = session.get(Article.class, id);
            if (article != null) {
                session.remove(article);
                System.out.println("Article deleted successfully.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}