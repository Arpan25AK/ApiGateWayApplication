package com.example.ApiGateWayApplication.ApiStrategy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;

public class GeminiStrategy implements AiModelStrategy{

    private final ChatClient chatClient;

    public GeminiStrategy(OpenAiChatModel geminiChatModel){
        this.chatClient = ChatClient.builder(geminiChatModel).build();
    }

    @Override
    public String getModelName(){
        return "GEMINI";
    }

    @Override
    public String generateResponse(String prompt){
        return chatClient.prompt(prompt).call().content();
    }

}
