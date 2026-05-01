package com.techpulse.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class NewsIngestionService{
    //creating a logger for this class
    //logmanager.getLogger() takes the class name as context
    //so every long msg shows exactly which class generated it
    private static final Logger logger=LogManager.getLogger(NewsIngestionService.class);

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

    private final RestTemplate restTemplate=new RestTemplate();


    @Scheduled(fixedRate=1800000)
    public void fetchAndStoreArticles() {
        //INFO-normal application event,ingestion is starting
        logger.info("News ingestion started at:{}", LocalDateTime.now());
        try {
            String fullUrl=newsApiUrl + newsApiKey;
// DEBUG — detailed tracing, only visible during development
            logger.debug("Calling NewsAPI with URL: {}", fullUrl);

            NewsApiResponse response = restTemplate.getForObject(
                fullUrl, NewsApiResponse.class);

            if (response != null && response.getArticles() != null) {
                List<NewsArticleDto> articles = response.getArticles();
                int savedCount = 0;
                int skippedCount = 0;

                // DEBUG — how many articles were returned
                logger.debug("NewsAPI returned {} articles",
                    articles.size());

                for (NewsArticleDto articleDto : articles) {

                    if (articleDto.getTitle() == null ||
                        articleDto.getTitle().equals("[Removed]")) {
                        // WARN — unexpected data, skipping
                        logger.warn("Skipping article with null or " +
                            "removed title from source: {}",
                            articleDto.getSource() != null
                            ? articleDto.getSource().getName()
                            : "Unknown");
                        skippedCount++;
                        continue;
                    }

                    if (articleDto.getUrl() == null) {
                        logger.warn("Skipping article with null URL: {}",
                            articleDto.getTitle());
                        skippedCount++;
                        continue;
                    }

                    if (articleRepository.existsByUrl(
                            articleDto.getUrl())) {
                        // DEBUG — duplicate detection, normal behaviour
                        logger.debug("Skipping duplicate article: {}",
                            articleDto.getTitle());
                        skippedCount++;
                        continue;
                    }

                    String sourceName = (articleDto.getSource() != null
                        && articleDto.getSource().getName() != null)
                        ? articleDto.getSource().getName()
                        : "Unknown";

                    Source source = findOrCreateSource(sourceName);

                    Category category = categoryRepository
                        .findById(1)
                        .orElse(null);

                    if (category == null) {
                        // WARN — category not found, unexpected
                        logger.warn("Default category with ID 1 not " +
                            "found. Article will have no category.");
                    }

                    Article article = new Article();
                    article.setTitle(articleDto.getTitle());
                    article.setSummary(articleDto.getDescription());
                    article.setUrl(articleDto.getUrl());
                    article.setPublishedAt(
                        parseDate(articleDto.getPublishedAt()));
                    article.setSource(source);
                    article.setCategory(category);
                    article.setType("EXTERNAL");
                    article.setStatus("APPROVED");

                    articleRepository.save(article);
                    savedCount++;

                    // DEBUG — individual article saved
                    logger.debug("Saved article: {}", articleDto.getTitle());
                }

                // INFO — ingestion complete, summary of results
                logger.info("News ingestion complete. Saved: {}, " +
                    "Skipped: {}", savedCount, skippedCount);

            } else {
                // WARN — API returned empty response
                logger.warn("NewsAPI returned null or empty response");
            }

        } catch (Exception e) {
            // ERROR — ingestion failed, this is a real problem
            logger.error("News ingestion failed: {}", e.getMessage(), e);
        }
    }

    private Source findOrCreateSource(String sourceName) {
        return sourceRepository.findByName(sourceName)
            .orElseGet(() -> {
                logger.debug("Creating new source: {}", sourceName);
                Source newSource = new Source();
                newSource.setName(sourceName);
                newSource.setWebsiteUrl("");
                newSource.setCountry("Unknown");
                return sourceRepository.save(newSource);
            });
    }

    private LocalDateTime parseDate(String dateString) {
        if (dateString == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateString,
                DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            logger.warn("Could not parse date: {}. Using current time.",
                dateString);
            return LocalDateTime.now();
        }
    }
}
       


