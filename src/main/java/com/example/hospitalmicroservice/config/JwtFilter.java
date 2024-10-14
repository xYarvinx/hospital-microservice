package com.example.hospitalmicroservice.config;


import com.example.hospitalmicroservice.dto.Error;
import com.example.hospitalmicroservice.dto.ErrorResponse;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import com.example.hospitalmicroservice.service.RabbitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final RabbitService rabbitService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = extractToken(request);
        if (token != null) {
            try {
                TokenValidationResponse validationResponse = rabbitService.sendTokenValidationRequest(token);
                if (!validationResponse.isValid()) {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }
            } catch (Exception e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error occurred");
                return;
            }
        }

        filterChain.doFilter(request, response);
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

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
