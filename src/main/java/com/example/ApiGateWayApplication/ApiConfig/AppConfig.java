package com.example.ApiGateWayApplication.ApiConfig;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }
}
