package com.example.ApiGateWayApplication.ApiController;

import com.example.ApiGateWayApplication.ApiDTO.AskRequest;
import com.example.ApiGateWayApplication.ApiDTO.AskResponse;
import com.example.ApiGateWayApplication.ApiService.RouterService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/call")
public class GateWayController {

    private final RouterService routerService;

    public GateWayController(RouterService routerService){
        this.routerService = routerService;
    }

    @PostMapping("prompt")
    public AskResponse aiCall(@RequestBody AskRequest req){
        String ans = routerService.routeAndExecute(req.getPrompt());
        return new AskResponse(ans);
    }

}
