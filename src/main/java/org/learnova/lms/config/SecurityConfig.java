package org.learnova.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //  auth.requestMatchers("/register", "/login").authenticated() this is 302
//      auth.requestMatchers("/register", "/login").permitAll() this is 302
//      String[] testPath = {"/user/**","/user/**/**", "/register", "/login"}; occurs this errror
//    org.springframework.web.util.pattern.PatternParseException: No more pattern data allowed after {*...} or ** pattern element
//Pattern cannot be null or empty ""  String[] testPath = {"/user/**","", "/register", "/login"};


    private static final String[] allowedPathsWithOutAuthentication = {"/api/v1/register/**", "/login"};
    private static final String[] allowedManagerPath = {
            "/manager/**",
            "/api/v1/user/",
            "/v3/api-docs/",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/api/v1/course/"}; //todo : why ok with api/v1/course/** and api/v1/user becuse PreAuthrize
    private static final String[] teacherPathAllowed = {"/api/v1/questions","/api/v1/exam/"};
    private static final String[] studentPathAllowed = {};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authrize -> authrize
                        .requestMatchers(allowedPathsWithOutAuthentication)
                        .permitAll()
                        .requestMatchers(allowedManagerPath).hasRole("MANAGER")
                        .requestMatchers(studentPathAllowed).hasRole("STUDENT")
                        .requestMatchers(teacherPathAllowed).hasRole("TEACHER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .formLogin(form -> form
                        .defaultSuccessUrl("/demo")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
