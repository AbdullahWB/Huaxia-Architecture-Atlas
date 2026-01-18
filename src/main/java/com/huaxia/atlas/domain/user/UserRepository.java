package com.huaxia.atlas.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsernameIgnoreCase(String username);

    Optional<UserAccount> findByEmailIgnoreCase(String email);

    Optional<UserAccount> findByResetToken(String resetToken);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    long countByRole(UserRole role);

    List<UserAccount> findTop10ByOrderByCreatedAtDesc();

    List<UserAccount> findByEmailIgnoreCaseIn(List<String> emails);
}
