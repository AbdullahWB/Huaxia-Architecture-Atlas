package com.huaxia.atlas.domain.user;

import com.huaxia.atlas.domain.user.dto.UserRegisterForm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostConstruct
    public void ensureDefaultAdmin() {
        if (adminUsername == null || adminUsername.isBlank()) {
            return;
        }
        if (repo.existsByUsernameIgnoreCase(adminUsername)) {
            return;
        }

        String password = (adminPassword == null || adminPassword.isBlank())
                ? "admin123"
                : adminPassword;

        UserAccount admin = new UserAccount();
        admin.setUsername(adminUsername);
        admin.setEmail(adminUsername + "@local");
        admin.setPasswordHash(encoder.encode(password));
        admin.setRole(UserRole.ADMIN);
        admin.setEnabled(true);
        repo.save(admin);
    }

    public Optional<UserAccount> findByLogin(String login) {
        if (login == null) {
            return Optional.empty();
        }
        String value = login.trim();
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return repo.findByUsernameIgnoreCase(value)
                .or(() -> repo.findByEmailIgnoreCase(value));
    }

    public Optional<UserAccount> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<UserAccount> findByResetToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return repo.findByResetToken(token.trim());
    }

    public boolean isResetTokenValid(String token) {
        Optional<UserAccount> userOpt = findByResetToken(token);
        if (userOpt.isEmpty()) {
            return false;
        }
        Instant expiresAt = userOpt.get().getResetTokenExpiresAt();
        return expiresAt != null && expiresAt.isAfter(Instant.now());
    }

    public long countAll() {
        return repo.count();
    }

    public long countUsers() {
        return repo.countByRole(UserRole.USER);
    }

    public long countAdmins() {
        return repo.countByRole(UserRole.ADMIN);
    }

    @Transactional
    public UserAccount register(UserRegisterForm form) {
        String username = form.getUsername().trim();
        String email = form.getEmail().trim();

        if (repo.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (repo.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(form.getPassword()));
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return repo.save(user);
    }

    @Transactional
    public Optional<String> createResetToken(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return repo.findByEmailIgnoreCase(email.trim()).map(user -> {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiresAt(Instant.now().plus(Duration.ofHours(2)));
            repo.save(user);
            return token;
        });
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        if (token == null || token.isBlank()) {
            return false;
        }
        Optional<UserAccount> userOpt = repo.findByResetToken(token.trim());
        if (userOpt.isEmpty()) {
            return false;
        }
        UserAccount user = userOpt.get();
        Instant expiresAt = user.getResetTokenExpiresAt();
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            return false;
        }
        user.setPasswordHash(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiresAt(null);
        repo.save(user);
        return true;
    }
}
