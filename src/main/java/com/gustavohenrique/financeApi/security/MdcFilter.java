package com.gustavohenrique.financeApi.security;

import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "userEmail";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Generate request ID
            String requestId = UUID.randomUUID().toString();
            MDC.put(REQUEST_ID, requestId);

            // Add request ID to response header for traceability
            response.addHeader("X-Request-ID", requestId);

            // Extract user info if authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof User user) {
                MDC.put(USER_ID, user.getId().toString());
                MDC.put(USER_EMAIL, user.getEmail());
            }

            filterChain.doFilter(request, response);
        } finally {
            // Always clear MDC to prevent memory leaks
            MDC.clear();
        }
    }
}
