package com.example.hospitalmicroservice.config;


import com.example.hospitalmicroservice.dto.Error;
import com.example.hospitalmicroservice.dto.ErrorResponse;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import com.example.hospitalmicroservice.service.RabbitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final RabbitService rabbitService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, java.io.IOException {
        final String token = extractToken((HttpServletRequest) servletRequest);
        if (token != null) {
            try {
                TokenValidationResponse validationResponse = rabbitService.sendTokenValidationRequest(token);
                if (!validationResponse.isValid()) {
                    sendErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }
           } catch (Exception e) {
                sendErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error occurred");
              return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(Error.builder()
                        .message(message)
                        .build())
                .build();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
