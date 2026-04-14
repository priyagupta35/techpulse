package com.techpulse.service;

import com.techpulse.model.CommunityPost;
import com.techpulse.repository.CommunityPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityPostService {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    public List<CommunityPost> getApprovedPosts() {
        return communityPostRepository.findByStatus("APPROVED");
    }

    // findById() already returns Optional<CommunityPost>
    // no need to wrap it in Optional.of() again
    public Optional<CommunityPost> getPostById(int id) {
        return communityPostRepository.findById(id);
    }

    public CommunityPost submitPost(CommunityPost post) {
        post.setStatus("PENDING");
        return communityPostRepository.save(post);
    }

    public CommunityPost updatePostStatus(int id, String status) {
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Post not found with id: " + id));
        post.setStatus(status);
        return communityPostRepository.save(post);
    }
}