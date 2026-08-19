package br.com.nutritionplatform.platform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

class DevAuthenticationFilter extends OncePerRequestFilter {
    static final String SUBJECT_HEADER = "X-Dev-Subject";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String subject = request.getHeader(SUBJECT_HEADER);
            if (subject == null || subject.isBlank()) {
                subject = "dev-nutritionist";
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(subject, "n/a", List.of()));
            response.setHeader("X-Development-Authentication", "active");
        }
        filterChain.doFilter(request, response);
    }
}
