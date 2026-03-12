package com.example.ApiGateWayApplication.ApiService;

import com.example.ApiGateWayApplication.ApiStrategy.AiModelStrategy;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class RouterService {

    private final List<AiModelStrategy> strategies;
    private final RedisTemplate redisTemplate;

    public RouterService(List<AiModelStrategy> strategies, RedisTemplate redisTemplate){
        this.strategies = strategies;
        this.redisTemplate = redisTemplate;
    }

    public Flux<String> routeAndExecute(String prompt){
        String cacheKey = "cache:prompt:" + Math.abs(prompt.hashCode());
        String cachedResponse = redisTemplate.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            log.info("⚡ FAST HIT! Returning cached response from Redis.");
            return Flux.just(cachedResponse); // Wrap the fast string in a Flux
        }

        String targetModel = determineBestModel(prompt);
        log.info("the receiving model would be {}",targetModel);

        for(AiModelStrategy strategy : strategies){
            if(strategy.getModelName().equalsIgnoreCase(targetModel)){
                StringBuilder fullResponseBuilder = new StringBuilder();

                return strategy.generateStreamResponse(prompt)
                        .doOnNext(chunk -> fullResponseBuilder.append(chunk))
                        .doOnComplete(() -> {
                            redisTemplate.opsForValue().set(cacheKey, fullResponseBuilder.toString(), Duration.ofHours(1));
                            log.info("💾 Saved new generated stream to Redis cache!");
                        });
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
