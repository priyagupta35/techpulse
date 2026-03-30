package com.techpulse.model;



public class Article {
    private int id;
    private String title;
    private String summary;
    private String url;
    private String publishedAt;
    private int sourceId;
    private int categoryId;
    private String type;
    private String status;

    public Article() {}

    public Article(String title, String summary, String url,
                   String publishedAt, int sourceId, 
                   int categoryId, String type, String status) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.publishedAt = publishedAt;
        this.sourceId = sourceId;
        this.categoryId = categoryId;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { 
        this.publishedAt = publishedAt; 
    }
    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { 
        this.categoryId = categoryId; 
    }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

