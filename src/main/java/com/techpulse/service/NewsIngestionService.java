package com.techpulse.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.techpulse.dto.NewsApiResponse;
import com.techpulse.dto.NewsArticleDto;
import com.techpulse.model.Article;
import com.techpulse.model.Category;
import com.techpulse.model.Source;
import com.techpulse.repository.ArticleRepository;
import com.techpulse.repository.CategoryRepository;
import com.techpulse.repository.SourceRepository;

@Service
public class NewsIngestionService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Value("${newsapi.key}")
    private String newsApiKey;

    @Value("${newsapi.url}")
    private String newsApiUrl;

    private RestTemplate restTemplate=new RestTemplate();

    // @Scheduled(fixedRate = 1800000) means Spring will call
    // this method automatically every 30 minutes
    // 1800000 milliseconds = 30 minutes
    // Spring calls this without any manual triggering
    @Scheduled(fixedRate = 1800000)
    public void fetchAndStoreArticles() {
        System.out.println("Starting news ingestion at: " 
            + LocalDateTime.now());
        try {
            // Build the full URL with API key appended
            String fullUrl = newsApiUrl + newsApiKey;

            // Make GET request to NewsAPI and map response
            // to our NewsApiResponse DTO automatically
            NewsApiResponse response = restTemplate.getForObject(
                fullUrl, NewsApiResponse.class);

            if (response != null && response.getArticles() != null) {
                List<NewsArticleDto> articles = response.getArticles();
                int savedCount = 0;

                for (NewsArticleDto articleDto : articles) {
                    // Skip articles with null or removed titles
                    if (articleDto.getTitle() == null || 
                        articleDto.getTitle().equals("[Removed]")) {
                        continue;
                    }

                    // Skip if article with same URL already exists
                    // prevents duplicate entries on repeated fetches
                    if (articleRepository.existsByUrl(
                            articleDto.getUrl())) {
                        continue;
                    }

                    // Find or create the source
                    Source source = findOrCreateSource(
                        articleDto.getSource().getName());

                    // Default all ingested articles to 
                    // "Technology" category — ID 1 from your
                    // categories table
                    Category category = categoryRepository
                        .findById(1)
                        .orElse(null);

                    // Map DTO fields to Article entity
                    Article article = new Article();
                    article.setTitle(articleDto.getTitle());
                    article.setSummary(articleDto.getDescription());
                    article.setUrl(articleDto.getUrl());
                    article.setPublishedAt(parseDate(
                        articleDto.getPublishedAt()));
                    article.setSource(source);
                    article.setCategory(category);
                    article.setType("EXTERNAL");
                    article.setStatus("APPROVED");

                    articleRepository.save(article);
                    savedCount++;
                }
                System.out.println("News ingestion complete. Saved " 
                    + savedCount + " new articles.");
            }
        } catch (RestClientException e) {
            System.err.println("News ingestion failed: " 
                + e.getMessage());
        }
    }
private Source findOrCreateSource(String sourceName) {
        return sourceRepository.findByName(sourceName)
            .orElseGet(() -> {
                Source newSource = new Source();
                newSource.setName(sourceName);
                newSource.setWebsiteUrl("");
                newSource.setCountry("Unknown");
                return sourceRepository.save(newSource);
            });
    }
   

    // Parses the ISO 8601 date string from NewsAPI
    // into a LocalDateTime object for database storage
    private LocalDateTime parseDate(String dateString) {
        if (dateString == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateString,
                DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
