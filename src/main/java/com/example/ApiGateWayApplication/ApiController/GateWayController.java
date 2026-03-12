package com.example.ApiGateWayApplication.ApiController;

import com.example.ApiGateWayApplication.ApiDTO.AskRequest;
import com.example.ApiGateWayApplication.ApiDTO.AskResponse;
import com.example.ApiGateWayApplication.ApiSecurity.RateLimiterService;
import com.example.ApiGateWayApplication.ApiService.RouterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/call")
public class GateWayController {

    private final RouterService routerService;
    private final RateLimiterService rateLimitService;

    public GateWayController(RouterService routerService, RateLimiterService rateLimiterService){
        this.routerService = routerService;
        this.rateLimitService = rateLimiterService;
    }

    @PostMapping(value = "/prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> aiCall(@RequestBody AskRequest req){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!rateLimitService.isValid(username)){
            return Flux.just("Error: User needs to wait for a minute for further AI calls");
        }

        return routerService.routeAndExecute(req.getPrompt(), username);
    }

}
