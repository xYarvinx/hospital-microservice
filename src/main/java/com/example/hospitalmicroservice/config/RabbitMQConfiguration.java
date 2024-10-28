package com.example.hospitalmicroservice.config;


import com.example.hospitalmicroservice.dto.RoleValidationRequest;
import com.example.hospitalmicroservice.dto.RoleValidationResponse;
import com.example.hospitalmicroservice.dto.TokenValidationRequest;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public TopicExchange roomExchange() {
        return new TopicExchange("roomExchange");
    }


    @Bean
    public Queue roomExistRequestQueue() {
        return new Queue("roomExistRequestQueue", true);
    }

    @Bean
    public Queue roomExistResponseQueue() {
        return new Queue("roomExistResponseQueue", true);
    }

    @Bean
    public Binding bindingRoomExistRequest(Queue roomExistRequestQueue, TopicExchange roomExchange) {
        return BindingBuilder.bind(roomExistRequestQueue).to(roomExchange).with("room.exist.request");
    }

    @Bean
    public Binding bindingRoomExistResponse(Queue roomExistResponseQueue, TopicExchange roomExchange) {
        return BindingBuilder.bind(roomExistResponseQueue).to(roomExchange).with("room.exist.response");
    }

    @Bean
    public TopicExchange hospitalExchange() {
        return new TopicExchange("hospitalExchange");
    }


    @Bean
    public Queue hospitalExistenceRequestQueue() {
        return new Queue("hospitalExistenceRequestQueue", true);
    }

    @Bean
    public Queue hospitalExistenceResponseQueue() {
        return new Queue("hospitalExistenceResponseQueue", true);
    }

    @Bean
    public Binding bindingHospitalExistenceRequest(Queue hospitalExistenceRequestQueue, TopicExchange hospitalExchange) {
        return BindingBuilder.bind(hospitalExistenceRequestQueue).to(hospitalExchange).with("hospital.exist.request");
    }

    @Bean
    public Binding bindingHospitalExistenceResponse(Queue hospitalExistenceResponseQueue, TopicExchange hospitalExchange) {
        return BindingBuilder.bind(hospitalExistenceResponseQueue).to(hospitalExchange).with("hospital.exist.response");
    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("TokenValidationRequest", TokenValidationRequest.class);
        idClassMapping.put("TokenValidationResponse", TokenValidationResponse.class);
        idClassMapping.put("RoleValidationRequest", RoleValidationRequest.class);
        idClassMapping.put("RoleValidationResponse", RoleValidationResponse.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
