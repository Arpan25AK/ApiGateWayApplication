package com.example.ApiGateWayApplication.ApiStrategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class GroqStrategy implements AiModelStrategy{

    private final ChatClient chatClient;

    public GroqStrategy(OpenAiChatModel groqChatModel) {
        this.chatClient = ChatClient.builder(groqChatModel).build();
    }

    @Override
    public String getModelName(){
        return "GROQ";
    }

    @Override
    public Flux<String> generateStreamResponse(String prompt){
        log.info("using Groq model to generate a response");

        return chatClient.prompt(prompt).stream().content();

        }
}
