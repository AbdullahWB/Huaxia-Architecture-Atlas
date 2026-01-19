package com.huaxia.atlas.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnbanRequestRepository extends JpaRepository<UnbanRequest, Long> {

    boolean existsByUserIdAndStatus(Long userId, UnbanRequestStatus status);

    List<UnbanRequest> findTop10ByStatusOrderByCreatedAtDesc(UnbanRequestStatus status);
}
