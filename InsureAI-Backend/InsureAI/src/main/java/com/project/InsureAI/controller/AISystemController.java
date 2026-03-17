package com.project.InsureAI.controller;

import com.project.InsureAI.dto.CreateAISystemRequest;
import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.service.AISystemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-system")
@CrossOrigin(origins = "*")
public class AISystemController {

    private final AISystemService aiSystemService;

    public AISystemController(AISystemService aiSystemService) {
        this.aiSystemService = aiSystemService;
    }

    @PostMapping
    public AISystem create(@RequestBody CreateAISystemRequest request) {
        return aiSystemService.createAISystem(request);
    }

    @GetMapping
    public List<AISystem> getMySystems() {
        return aiSystemService.getMyAISystems();
    }

    @GetMapping("/{id}")
    public AISystem getById(@PathVariable Long id) {
        return aiSystemService.getAISystemById(id);
    }
}