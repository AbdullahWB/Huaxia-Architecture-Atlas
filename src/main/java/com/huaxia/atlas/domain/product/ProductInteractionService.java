package com.huaxia.atlas.domain.product;

import com.huaxia.atlas.domain.user.UserAccount;
import com.huaxia.atlas.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductInteractionService {

    private final ProductRepository productRepository;
    private final ProductReviewRepository reviewRepository;
    private final ProductLikeRepository likeRepository;
    private final UserRepository userRepository;

    public ProductInteractionService(
            ProductRepository productRepository,
            ProductReviewRepository reviewRepository,
            ProductLikeRepository likeRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<ProductReview> listReviews(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public long countReviews(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    public long countLikes(Long productId) {
        return likeRepository.countByProductId(productId);
    }

    public boolean hasLiked(Long productId, Long userId) {
        return likeRepository.findByProductIdAndUserId(productId, userId).isPresent();
    }

    @Transactional
    public void addReview(Long productId, Long userId, int rating, String content) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content.trim());
        reviewRepository.save(review);
    }

    @Transactional
    public boolean toggleLike(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var existing = likeRepository.findByProductIdAndUserId(productId, userId);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false;
        }

        ProductLike like = new ProductLike();
        like.setProduct(product);
        like.setUser(user);
        likeRepository.save(like);
        return true;
    }

    public BigDecimal averageRating(Long productId) {
        List<ProductReview> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        if (reviews.isEmpty()) {
            return BigDecimal.ZERO;
        }
        int total = reviews.stream().mapToInt(ProductReview::getRating).sum();
        return BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
    }
}
