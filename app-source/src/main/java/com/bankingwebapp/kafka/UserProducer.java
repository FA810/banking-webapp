package com.bankingwebapp.kafka;

import com.bankingwebapp.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "user-create-topic";

    public void sendUser(UserDTO userDTO) {
        try {
            String userJson = objectMapper.writeValueAsString(userDTO);
            kafkaTemplate.send(TOPIC, userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize UserDTO", e);
        }
    }
}