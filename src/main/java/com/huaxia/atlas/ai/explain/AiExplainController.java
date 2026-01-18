package com.huaxia.atlas.ai.explain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/explain")
public class AiExplainController {

    private final AiExplainService aiExplainService;

    public AiExplainController(AiExplainService aiExplainService) {
        this.aiExplainService = aiExplainService;
    }

    @GetMapping("/buildings/{id}")
    public ResponseEntity<AiExplainResponse> explainBuilding(@PathVariable("id") Long id) {
        return aiExplainService.explainBuilding(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<AiExplainResponse> explainPost(@PathVariable("id") Long id) {
        return aiExplainService.explainPost(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<AiExplainResponse> explainProduct(@PathVariable("id") Long id) {
        return aiExplainService.explainProduct(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
