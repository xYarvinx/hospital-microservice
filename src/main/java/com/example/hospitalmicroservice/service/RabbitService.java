package com.example.hospitalmicroservice.service;


import com.example.hospitalmicroservice.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;

    public TokenValidationResponse sendTokenValidationRequest(String token) {
        String correlationId = UUID.randomUUID().toString();
        TokenValidationRequest request = new TokenValidationRequest(token, correlationId);

        rabbitTemplate.convertAndSend("authExchange", "auth.request." + correlationId, request);

        Message responseMessage = rabbitTemplate.receive("authResponseQueue", 5000);
        if (responseMessage != null) {
            return (TokenValidationResponse) rabbitTemplate.getMessageConverter().fromMessage(responseMessage);
        } else {
            throw new RuntimeException("No response received within timeout period");
        }
    }


    public RoleValidationResponse sendRoleValidationRequest(String token, List<String> roles) {
        String correlationId = UUID.randomUUID().toString();
        RoleValidationRequest request = new RoleValidationRequest(token, correlationId, roles);

        rabbitTemplate.convertAndSend("roleExchange", "role.request." + correlationId, request);

        Message responseMessage = rabbitTemplate.receive("roleResponseQueue", 5000);
        if (responseMessage != null) {
            return (RoleValidationResponse) rabbitTemplate.getMessageConverter().fromMessage(responseMessage);
        } else {
            throw new RuntimeException("No response received within timeout period");
        }
    }


}
