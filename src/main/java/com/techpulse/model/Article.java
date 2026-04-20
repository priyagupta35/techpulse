package com.techpulse.model;

//import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="articles")
public class Article {
 public enum Status {
    PENDING,
    APPROVED,
    REJECTED
}
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="title",nullable=false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name="summary",columnDefinition="TEXT")
    private String summary;

    @Column(name="content", columnDefinition="TEXT")
     private String content;

    @Column(name="url",length=500)
    private String url;

    @Column(name="published_at")
    private LocalDateTime publishedAt;

   @ManyToOne
   @JoinColumn(name="source_id")
   private Source source;

   @Column(name="type",columnDefinition="ENUM('EXTERNAL','COMMUNITY')")
   private String type;

   @Enumerated(EnumType.STRING)
   @Column(name="status")  private Status status;
    public Article() {}
  //getter & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
    return content;
}

public void setContent(String content) {
    this.content = content;
}

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
}