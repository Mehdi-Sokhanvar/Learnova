package org.learnova.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] allowedPathsWithOutAuthentication = {"/api/register/**", "/login"};
    private static final String[] allowedManagerPath = {
            "/manager/**",
            "/api/v1/user/",
            "/v3/api-docs/",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/api/v1/course/"}; //todo : why ok with api/v1/course/** and api/v1/user becuse PreAuthrize
    private static final String[] teacherPathAllowed = {"/api/v1/questions","/api/v1/exam/"};
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
                        .requestMatchers(allowedPathsWithOutAuthentication)
                        .permitAll()
                        .requestMatchers(allowedManagerPath).hasRole("MANAGER")
                        .requestMatchers(studentPathAllowed).hasRole("STUDENT")
                        .requestMatchers(teacherPathAllowed).hasRole("TEACHER")
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

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
