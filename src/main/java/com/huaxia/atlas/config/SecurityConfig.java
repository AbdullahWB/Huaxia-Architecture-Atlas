package com.huaxia.atlas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/", "/explore/**", "/buildings/**", "/search/**", "/chat/**",
                        "/posts/**", "/contact/**")
                .permitAll()

                // Static assets + uploaded images
                .requestMatchers("/css/**", "/js/**", "/img/**", "/uploads/**").permitAll()

                // Login page must be public
                .requestMatchers("/admin/login").permitAll()

                // Protect admin area
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Everything else: allow (or change to authenticated() if you want)
                .anyRequest().permitAll());

        http.formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureUrl("/admin/login?error")
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/"));

        // Keep CSRF enabled (default). Later, your Thymeleaf forms will include CSRF
        // tokens.
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
