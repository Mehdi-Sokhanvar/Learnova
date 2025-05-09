package org.learnova.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] allowedPathsWithOutAuthentication = {"/api/register/**", "/api/auth/login"};
    private static final String[] adminPathAllowed = {"/api/v1/courses/**"};
    private static final String[] teacherPathAllowed = {"/api/v1/questions","/api/v1/exam/","/api/v1/user/courses"};
    private static final String[] studentPathAllowed = {};
    private final JwtAuthorizationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authrize -> authrize
                        .requestMatchers(allowedPathsWithOutAuthentication).permitAll()
                        .requestMatchers(adminPathAllowed).hasAuthority("ADMIN")
                        .requestMatchers(studentPathAllowed).hasAuthority("STUDENT")
                        .requestMatchers(teacherPathAllowed).hasAuthority("TEACHER")

                ).sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


}
