package com.example.ApiGateWayApplication.ApiService;

import com.example.ApiGateWayApplication.ApiStrategy.AiModelStrategy;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RouterService {

    private final List<AiModelStrategy> strategies;

    public RouterService(List<AiModelStrategy> strategies){
        this.strategies = strategies;
    }

    public String routeAndExecute(String prompt){
        String targetModel = determineBestModel(prompt);
        log.info("the receiving model would be {}",targetModel);

        for(AiModelStrategy strategy : strategies){
            if(strategy.getModelName().equalsIgnoreCase(targetModel)){
                return strategy.generateResponse(prompt);
            }
        }

        throw new RuntimeException("No AI Strategy found for model: " + targetModel);
    }

    private String determineBestModel(String prompt){
        if(prompt.length() > 500
                || prompt.toLowerCase().contains("summarize")
                || prompt.toLowerCase().contains("define")
                || prompt.toLowerCase().contains("analyze")
                || prompt.toLowerCase().contains("complex")
        ){
            return "GEMINI";
        }else{
            return "GROQ";
        }

    }
}
