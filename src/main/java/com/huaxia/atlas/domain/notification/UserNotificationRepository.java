package com.huaxia.atlas.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    List<UserNotification> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsRead(Long userId, boolean isRead);
}
