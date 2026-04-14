// CommunityPostController.java
package com.techpulse.controller;

import com.techpulse.model.CommunityPost;
import com.techpulse.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/community-posts")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    // GET http://localhost:8080/api/community-posts
    // Returns only approved community posts
    @GetMapping
    public ResponseEntity<List<CommunityPost>> getApprovedPosts() {
        return ResponseEntity.ok(
            communityPostService.getApprovedPosts());
    }

    // GET http://localhost:8080/api/community-posts/1
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPost> getPostById(
            @PathVariable int id) {
        return communityPostService.getPostById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:8080/api/community-posts
    // Any user submits a new post — starts as PENDING
    @PostMapping
    public ResponseEntity<CommunityPost> submitPost(
            @RequestBody CommunityPost post) {
        return ResponseEntity.ok(
            communityPostService.submitPost(post));
    }

    // PUT http://localhost:8080/api/community-posts/1/status?status=APPROVED
    // Admin endpoint to approve or reject a post
    @PutMapping("/{id}/status")
    public ResponseEntity<CommunityPost> updateStatus(
            @PathVariable int id,
            @RequestParam String status) {
        return ResponseEntity.ok(
            communityPostService.updatePostStatus(id, status));
    }
}
