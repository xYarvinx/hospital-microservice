package com.example.hospitalmicroservice.service;


import com.example.hospitalmicroservice.dto.TokenValidationRequest;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public com.example.hospitalmicroservice.dto.TokenValidationResponse sendTokenValidationRequest(String token) {
        String correlationId = UUID.randomUUID().toString();


        TokenValidationRequest request = new TokenValidationRequest(token, correlationId);

        rabbitTemplate.convertAndSend("authExchange", "auth.request", request);
        Message message = rabbitTemplate.receive("authResponseQueue", 5000);
        if (message != null) {
            TokenValidationResponse response = (TokenValidationResponse) rabbitTemplate.getMessageConverter().fromMessage(message);
            return response;
        } else {
            throw new RuntimeException("No response received within timeout period");
        }
    }

}
