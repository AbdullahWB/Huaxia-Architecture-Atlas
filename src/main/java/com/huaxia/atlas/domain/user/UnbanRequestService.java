package com.huaxia.atlas.domain.user;

import com.huaxia.atlas.domain.notification.UserNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnbanRequestService {

    private final UnbanRequestRepository repository;
    private final UserNotificationService notificationService;

    public UnbanRequestService(UnbanRequestRepository repository, UserNotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public List<UnbanRequest> latestPending(int limit) {
        List<UnbanRequest> all = repository.findTop10ByStatusOrderByCreatedAtDesc(UnbanRequestStatus.PENDING);
        if (all.size() <= limit) {
            return all;
        }
        return all.subList(0, limit);
    }

    public boolean hasPending(Long userId) {
        return repository.existsByUserIdAndStatus(userId, UnbanRequestStatus.PENDING);
    }

    @Transactional
    public UnbanRequest create(UserAccount user, String reason) {
        UnbanRequest request = new UnbanRequest();
        request.setUser(user);
        request.setEmail(user.getEmail());
        request.setReason(reason == null || reason.isBlank() ? null : reason.trim());
        request.setStatus(UnbanRequestStatus.PENDING);
        return repository.save(request);
    }

    @Transactional
    public boolean approve(Long requestId, UserService userService) {
        return repository.findById(requestId).map(request -> {
            request.setStatus(UnbanRequestStatus.APPROVED);
            repository.save(request);
            userService.setEnabled(request.getUser().getId(), true);
            notificationService.createNotification(
                    request.getUser().getId(),
                    "Unban approved",
                    "Your account has been re-enabled. You can now log in again."
            );
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean reject(Long requestId) {
        return repository.findById(requestId).map(request -> {
            request.setStatus(UnbanRequestStatus.REJECTED);
            repository.save(request);
            return true;
        }).orElse(false);
    }
}
