package com.huaxia.atlas.ai.explain;

import com.huaxia.atlas.ai.chat.DeepSeekClient;
import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingService;
import com.huaxia.atlas.domain.message.Message;
import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.post.Post;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.product.Product;
import com.huaxia.atlas.domain.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AiExplainService {

    private final DeepSeekClient deepSeekClient;
    private final BuildingService buildingService;
    private final PostService postService;
    private final ProductService productService;
    private final MessageService messageService;

    public AiExplainService(
            DeepSeekClient deepSeekClient,
            BuildingService buildingService,
            PostService postService,
            ProductService productService,
            MessageService messageService) {
        this.deepSeekClient = deepSeekClient;
        this.buildingService = buildingService;
        this.postService = postService;
        this.productService = productService;
        this.messageService = messageService;
    }

    @Transactional(readOnly = true)
    public Optional<AiExplainResponse> explainBuilding(Long id) {
        return buildingService.get(id).map(building -> {
            String prompt = buildBuildingPrompt(building);
            String summary = deepSeekClient.generateText(prompt);
            return new AiExplainResponse(safe(building.getName()), summary);
        });
    }

    @Transactional(readOnly = true)
    public Optional<AiExplainResponse> explainPost(Long id) {
        return postService.getApproved(id).map(post -> {
            String prompt = buildPostPrompt(post);
            String summary = deepSeekClient.generateText(prompt);
            return new AiExplainResponse(safe(post.getTitle()), summary);
        });
    }

    @Transactional(readOnly = true)
    public Optional<AiExplainResponse> explainProduct(Long id) {
        return productService.get(id).map(product -> {
            String prompt = buildProductPrompt(product);
            String summary = deepSeekClient.generateText(prompt);
            return new AiExplainResponse(safe(product.getName()), summary);
        });
    }

    @Transactional(readOnly = true)
    public Optional<String> draftMessageReply(Long id) {
        return messageService.get(id).map(message -> deepSeekClient.generateText(buildMessagePrompt(message)));
    }

    private String buildBuildingPrompt(Building building) {
        return """
You are an expert guide for ancient Chinese architecture (pre-1911).
Explain the site for a general audience using ONLY the provided data.
If a field is missing, say it is unknown.

BUILDING DATA:
Name: %s
Dynasty: %s
Type: %s
Location: %s
Year built: %s
Tags: %s
Description: %s

RESPONSE FORMAT:
- 2-3 sentence paragraph.
- Then 2-3 bullet points for key facts.
""".formatted(
                safe(building.getName()),
                safe(building.getDynasty()),
                safe(building.getType()),
                safe(building.getLocation()),
                safe(building.getYearBuilt()),
                safe(building.getTags()),
                trim(building.getDescription(), 600)
        );
    }

    private String buildPostPrompt(Post post) {
        return """
You are summarizing a community post about architecture.
Use ONLY the provided text and do not add new facts.

POST:
Title: %s
Author: %s
Content: %s

RESPONSE FORMAT:
- 1-2 sentence summary.
- Then 2-3 bullet points for key ideas.
""".formatted(
                safe(post.getTitle()),
                safe(post.getAuthorName()),
                trim(post.getContent(), 900)
        );
    }

    private String buildProductPrompt(Product product) {
        return """
You are describing a catalog product for a study-focused store.
Use ONLY the provided data and keep it concise.

PRODUCT:
Name: %s
Description: %s
Price: $%s
Stock: %s

RESPONSE FORMAT:
- 2-3 short sentences.
- Then 2 bullet points for key highlights.
""".formatted(
                safe(product.getName()),
                trim(product.getDescription(), 600),
                product.getPrice() == null ? "-" : product.getPrice(),
                product.getStock()
        );
    }

    private String buildMessagePrompt(Message message) {
        return """
You are an admin assistant for Huaxia Atlas.
Draft a polite reply to the user's message.
Be concise, professional, and ask for missing details if needed.
Do not promise actions you cannot perform.

MESSAGE:
From: %s
Email: %s
Subject: %s
Message: %s

RESPONSE FORMAT:
- 3-5 sentences in plain text.
""".formatted(
                safe(message.getName()),
                safe(message.getEmail()),
                safe(message.getSubject()),
                trim(message.getMessage(), 1200)
        );
    }

    private String safe(String value) {
        if (value == null) {
            return "-";
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? "-" : trimmed;
    }

    private String trim(String value, int max) {
        if (value == null) {
            return "-";
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return "-";
        }
        if (trimmed.length() <= max) {
            return trimmed;
        }
        return trimmed.substring(0, max) + "...";
    }
}
