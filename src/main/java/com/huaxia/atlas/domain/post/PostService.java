package com.huaxia.atlas.domain.post;

import com.huaxia.atlas.domain.post.dto.PostCreateForm;
import com.huaxia.atlas.domain.post.dto.PostModerationForm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository repo;

    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    // ---------------------------
    // Public operations
    // ---------------------------

    public Page<Post> listApproved(int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.findByStatusOrderByCreatedAtDesc(PostStatus.APPROVED, pageable);
    }

    public Optional<Post> getApproved(Long id) {
        return repo.findByIdAndStatus(id, PostStatus.APPROVED);
    }

    @Transactional
    public Post create(PostCreateForm form) {
        Post p = new Post();
        p.setTitle(form.getTitle().trim());
        p.setContent(form.getContent().trim());
        p.setAuthorName(blankToNull(form.getAuthorName()));
        p.setAuthorEmail(blankToNull(form.getAuthorEmail()));
        p.setStatus(PostStatus.PENDING);
        return repo.save(p);
    }

    // ---------------------------
    // Admin operations
    // ---------------------------

    public Page<Post> listByStatus(PostStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public long countPending() {
        return repo.countByStatus(PostStatus.PENDING);
    }

    @Transactional
    public Optional<Post> moderate(Long postId, PostModerationForm form) {
        return repo.findById(postId).map(p -> {
            p.setStatus(form.getStatus());
            return repo.save(p);
        });
    }

    // ---------------------------
    // helpers
    // ---------------------------
    private String blankToNull(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
