package com.techpulse.service;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;
import com.techpulse.model.Article;
import com.techpulse.model.Category;
import com.techpulse.model.Source;
import com.techpulse.repository.ArticleRepository;
import com.techpulse.repository.CategoryRepository;
import com.techpulse.repository.SourceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewsIngestionServiceTest {

    @Test
    void trailingSlashStoresOneRow() throws Exception {
        List<String> stored = ingest(
                "https://example.com/java-21",
                "https://example.com/java-21/");

        assertEquals(List.of("https://example.com/java-21"), stored);
    }

    @Test
    void hostCaseStoresOneRow() throws Exception {
        List<String> stored = ingest(
                "https://Example.COM/java-21",
                "https://example.com/java-21");

        assertEquals(List.of("https://example.com/java-21"), stored);
    }

    @Test
    void fragmentStoresOneRow() throws Exception {
        List<String> stored = ingest(
                "https://example.com/java-21#section-2",
                "https://example.com/java-21");

        assertEquals(List.of("https://example.com/java-21"), stored);
    }

    @Test
    void utmParamsStoreOneRow() throws Exception {
        List<String> stored = ingest(
                "https://example.com/java-21?utm_source=newsletter&utm_medium=email",
                "https://example.com/java-21");

        assertEquals(List.of("https://example.com/java-21"), stored);
    }

    @Test
    void differentIdParamsStoreTwoRows() throws Exception {
        List<String> stored = ingest(
                "https://example.com/java-21?id=5",
                "https://example.com/java-21?id=6");

        assertEquals(List.of(
                "https://example.com/java-21?id=5",
                "https://example.com/java-21?id=6"), stored);
    }

    private List<String> ingest(String url1, String url2) throws Exception {
        String feed = "{\"status\":\"ok\",\"totalResults\":2,\"articles\":["
                + "{\"title\":\"Java 21 released\",\"description\":\"First copy\","
                + "\"url\":\"" + url1 + "\",\"publishedAt\":\"2026-01-01T10:00:00Z\","
                + "\"source\":{\"id\":\"ex\",\"name\":\"Example\"}},"
                + "{\"title\":\"Java 21 released\",\"description\":\"Second copy\","
                + "\"url\":\"" + url2 + "\",\"publishedAt\":\"2026-01-01T10:05:00Z\","
                + "\"source\":{\"id\":\"ex2\",\"name\":\"Example\"}}]}";

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/news", exchange -> {
            byte[] body = feed.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();

        try {
            List<Article> rows = new ArrayList<>();
            Set<String> urls = new HashSet<>();

            ArticleRepository articleRepo = mock(ArticleRepository.class);
            when(articleRepo.existsByUrl(anyString()))
                    .thenAnswer(inv -> urls.contains(inv.getArgument(0)));
            when(articleRepo.save(any(Article.class))).thenAnswer(inv -> {
                Article article = inv.getArgument(0);
                rows.add(article);
                urls.add(article.getUrl());
                return article;
            });

            CategoryRepository categoryRepo = mock(CategoryRepository.class);
            when(categoryRepo.findById(anyInt()))
                    .thenReturn(Optional.of(new Category("Tech")));

            SourceRepository sourceRepo = mock(SourceRepository.class);
            when(sourceRepo.findByName(anyString()))
                    .thenReturn(Optional.of(new Source("Example", "", "Unknown")));

            NewsIngestionService service = new NewsIngestionService();
            set(service, "articleRepository", articleRepo);
            set(service, "categoryRepository", categoryRepo);
            set(service, "sourceRepository", sourceRepo);
            set(service, "newsApiKey", "test");
            set(service, "newsApiUrl",
                    "http://127.0.0.1:" + server.getAddress().getPort()
                            + "/news?apiKey=");

            service.fetchAndStoreArticles();

            return rows.stream()
                    .map(Article::getUrl)
                    .collect(Collectors.toList());
        } finally {
            server.stop(0);
        }
    }

    private void set(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
