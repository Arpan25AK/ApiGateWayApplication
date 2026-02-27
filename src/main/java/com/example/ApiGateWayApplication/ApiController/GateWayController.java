package com.example.ApiGateWayApplication.ApiController;

import com.example.ApiGateWayApplication.ApiDTO.AskRequest;
import com.example.ApiGateWayApplication.ApiDTO.AskResponse;
import com.example.ApiGateWayApplication.ApiSecurity.RateLimiterService;
import com.example.ApiGateWayApplication.ApiService.RouterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/call")
public class GateWayController {

    private final RouterService routerService;
    private final RateLimiterService rateLimitService;

    public GateWayController(RouterService routerService, RateLimiterService rateLimiterService){
        this.routerService = routerService;
        this.rateLimitService = rateLimiterService;
    }

    @PostMapping("prompt")
    public ResponseEntity<AskResponse> aiCall(@RequestBody AskRequest req){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!rateLimitService.isValid(username)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).
                    body(new AskResponse("user need to wait for a minute for further ai calls"));
        }

        String ans = routerService.routeAndExecute(req.getPrompt());

        // 2. Wrapped the successful answer in an OK (200) ResponseEntity!
        return ResponseEntity.ok(new AskResponse(ans));
    }

}
