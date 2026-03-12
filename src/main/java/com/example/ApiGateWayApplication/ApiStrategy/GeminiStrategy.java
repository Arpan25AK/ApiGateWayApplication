package com.example.ApiGateWayApplication.ApiStrategy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GeminiStrategy implements AiModelStrategy{

    private final ChatClient chatClient;

    public GeminiStrategy(GoogleGenAiChatModel geminiChatModel){
        this.chatClient = ChatClient.builder(geminiChatModel).build();
    }

    @Override
    public String getModelName(){
        return "GEMINI";
    }

    @Override
    public Flux<String> generateStreamResponse(String prompt){
        return chatClient.prompt(prompt).stream().content();
    }

}
