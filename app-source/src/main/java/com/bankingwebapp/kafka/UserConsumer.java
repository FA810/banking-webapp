package com.bankingwebapp.kafka;

import com.bankingwebapp.dto.UserDTO;
import com.bankingwebapp.entity.User;
import com.bankingwebapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-create-topic", groupId = "user-group")
    public void consume(String message) {
        try {
            UserDTO dto = objectMapper.readValue(message, UserDTO.class);
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(dto.getPassword());
            user.setRole(dto.getRole());
            user.setEmail(dto.getEmail());
            userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
