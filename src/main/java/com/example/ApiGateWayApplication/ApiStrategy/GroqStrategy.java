package com.example.ApiGateWayApplication.ApiStrategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class GroqStrategy implements AiModelStrategy{

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public GroqStrategy(OpenAiChatModel groqChatModel, ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(groqChatModel).build();
        this.chatMemory = chatMemory;
    }

    @Override
    public String getModelName(){
        return "GROQ";
    }

    @Override
    public Flux<String> generateStreamResponse(String prompt){
        log.info("using Groq model to generate a response");

        return chatClient.prompt(prompt).advisors(new MessageChatMemoryAdvisor(chatMemory, conversationId, 10)).stream().content();

        }
}
