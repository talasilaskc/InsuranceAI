package com.project.InsureAI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:4200"));
                    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())) // Support for H2 console

                .authorizeHttpRequests(auth -> auth

        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                        .requestMatchers("/api/test/**").permitAll() // For testing purposes

        // ⭐ VERY IMPORTANT FOR CORS
        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

        // ROLE SPECIFIC
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/underwriter/**").hasRole("UNDERWRITER")
        .requestMatchers("/api/officer/**").hasRole("CLAIMS_OFFICER")

        // COMPANY DOMAIN
        .requestMatchers("/api/ai-system/**","/api/risk-assessments/**",
                "/api/premium/**","/api/company/dashboard/**").hasRole("COMPANY")

        // CLAIMS
        .requestMatchers("/api/claims/submit", "/api/claims/company", "/api/claims/policy/**").hasRole("COMPANY")
        .requestMatchers("/api/claims/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/claims/*/documents/**", "/api/claims/documents/*/download")
                .hasAnyRole("COMPANY", "CLAIMS_OFFICER", "ADMIN")

        // SHARED
        .requestMatchers("/api/policies/**").hasAnyRole("COMPANY","UNDERWRITER")
        .requestMatchers("/api/policy-types/**").hasAnyRole("ADMIN","COMPANY")



        .anyRequest().authenticated()
)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}