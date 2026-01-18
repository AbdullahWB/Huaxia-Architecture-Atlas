package com.huaxia.atlas.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    Optional<ProductLike> findByProductIdAndUserId(Long productId, Long userId);

    long countByProductId(Long productId);
}
