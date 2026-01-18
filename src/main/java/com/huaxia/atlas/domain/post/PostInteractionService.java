package com.huaxia.atlas.domain.post;

import com.huaxia.atlas.domain.user.UserAccount;
import com.huaxia.atlas.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostInteractionService {

    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final PostLikeRepository likeRepository;
    private final UserRepository userRepository;

    public PostInteractionService(
            PostRepository postRepository,
            PostCommentRepository commentRepository,
            PostLikeRepository likeRepository,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<PostComment> listComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    public long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean hasLiked(Long postId, Long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

    @Transactional
    public void addComment(Long postId, Long userId, String content, Long parentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content.trim());

        if (parentId != null) {
            PostComment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalArgumentException("Parent comment does not belong to this post");
            }
            comment.setParent(parent);
        }

        commentRepository.save(comment);
    }

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var existing = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false;
        }

        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
        return true;
    }
}
