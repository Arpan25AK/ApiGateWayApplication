package com.example.ApiGateWayApplication.ApiStrategy;

import reactor.core.publisher.Flux;

public interface AiModelStrategy {

    String getModelName();

    Flux<String> generateStreamResponse(String prompt);

}
