    package org.learnova.lms.config;
    
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.learnova.lms.service.login.CustomUserDetails;
    import org.learnova.lms.service.login.LoginServiceImpl;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    
    import java.io.IOException;
    
    
    @Component
    public class JwtAuthorizationFilter extends OncePerRequestFilter {
    
        private final JwtTokenProvider jwtProvider;
        private final LoginServiceImpl userDetailsService;
    
        public JwtAuthorizationFilter(JwtTokenProvider jwtProvider, LoginServiceImpl userDetailsService) {
            this.jwtProvider = jwtProvider;
            this.userDetailsService = userDetailsService;
        }
    
    
        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                        FilterChain chain) throws ServletException, IOException {
            String header = req.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    String username = jwtProvider.getUsername(token);
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    var auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    return;
                }
            }
            chain.doFilter(req, res);
        }
    }
