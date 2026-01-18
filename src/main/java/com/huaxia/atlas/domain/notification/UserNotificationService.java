package com.huaxia.atlas.domain.notification;

import com.huaxia.atlas.domain.user.UserAccount;
import com.huaxia.atlas.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserNotificationService {

    private final UserNotificationRepository repository;
    private final UserRepository userRepository;

    public UserNotificationService(UserNotificationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<UserNotification> latestForUser(Long userId) {
        return repository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }

    public long countUnread(Long userId) {
        return repository.countByUserIdAndIsRead(userId, false);
    }

    @Transactional
    public void createNotification(Long userId, String title, String body) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserNotification note = new UserNotification();
        note.setUser(user);
        note.setTitle(title.trim());
        note.setBody(body.trim());
        note.setRead(false);
        repository.save(note);
    }
}
