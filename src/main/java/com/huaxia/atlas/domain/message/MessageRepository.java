package com.huaxia.atlas.domain.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByIsReadOrderByCreatedAtDesc(boolean isRead, Pageable pageable);

    long countByIsRead(boolean isRead);
}
