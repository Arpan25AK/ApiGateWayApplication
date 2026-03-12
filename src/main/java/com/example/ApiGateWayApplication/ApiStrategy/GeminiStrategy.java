package com.example.ApiGateWayApplication.ApiStrategy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GeminiStrategy implements AiModelStrategy{

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public GeminiStrategy(GoogleGenAiChatModel geminiChatModel, ChatMemory chatMemory){
        this.chatClient = ChatClient.builder(geminiChatModel).build();
        this.chatMemory = chatMemory;
    }

    @Override
    public String getModelName(){
        return "GEMINI";
    }

    @Override
    public Flux<String> generateStreamResponse(String prompt){

        return chatClient.prompt(prompt).advisors(new MessageChatMemoryAdvisor(chatMemory, conversationId, 5)).stream().content();
    }

}
