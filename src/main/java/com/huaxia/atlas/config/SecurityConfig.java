package com.huaxia.atlas.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler successHandler) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/forgot", "/reset", "/admin/login").permitAll()

                // User dashboard
                .requestMatchers("/dashboard/**").hasAnyRole("USER", "ADMIN")

                // Authenticated user actions
                .requestMatchers("/posts/new", "/posts/*/comment", "/posts/*/like").authenticated()
                .requestMatchers("/buildings/*/comment", "/buildings/*/like").authenticated()
                .requestMatchers("/orders/**", "/products/*/buy").authenticated()

                // Admin area
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Public pages
                .requestMatchers("/", "/explore/**", "/buildings/**", "/search/**", "/chat/**",
                        "/posts/**", "/contact/**", "/products/**")
                .permitAll()

                // Static assets + uploaded images
                .requestMatchers("/css/**", "/js/**", "/img/**", "/uploads/**").permitAll()

                .anyRequest().permitAll());

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?error")
                .permitAll());

        http.logout(logout -> logout
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", "GET"),
                        new AntPathRequestMatcher("/logout", "POST")
                ))
                .logoutSuccessUrl("/login?logout")
                .permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                if (isAdmin) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    response.sendRedirect("/dashboard");
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
