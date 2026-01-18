package com.huaxia.atlas.ai.chat;

import com.huaxia.atlas.ai.search.SearchResult;
import com.huaxia.atlas.ai.search.SearchService;
import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Service
public class ChatService {

    private final DeepSeekClient deepSeekClient;
    private final SearchService searchService;
    private final BuildingRepository buildingRepository;
    private final JdbcTemplate jdbcTemplate;

    public ChatService(
            DeepSeekClient deepSeekClient,
            SearchService searchService,
            BuildingRepository buildingRepository,
            JdbcTemplate jdbcTemplate) {
        this.deepSeekClient = deepSeekClient;
        this.searchService = searchService;
        this.buildingRepository = buildingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * “RAG-lite”:
     * - Find top matching buildings for the question
     * - Include short snippets as context
     * - Ask Gemini to answer using that context
     */
    @Transactional(readOnly = true)
    public ChatAnswer ask(String question) {
        String q = question == null ? "" : question.trim();
        if (q.isEmpty()) {
            return new ChatAnswer("", "Please enter a question.");
        }

        List<SearchResult> hits = searchService.search(q, 5);

        String prompt = buildPrompt(q, hits);
        String answer = deepSeekClient.generateText(prompt);

        // Best-effort log (don’t fail the user if logging fails)
        try {
            jdbcTemplate.update(
                    "INSERT INTO chat_logs (user_question, model_answer) VALUES (?, ?)",
                    q, answer);
        } catch (Exception ignored) {
            // ignore
        }

        return new ChatAnswer(prompt, answer);
    }

    private String buildPrompt(String question, List<SearchResult> hits) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert guide for ancient Chinese architecture (pre-1911). ")
                .append("Answer the user's question using ONLY the provided context. ")
                .append("If the context is insufficient, say so and suggest what to search.\n\n");

        sb.append("USER QUESTION:\n").append(question).append("\n\n");

        sb.append("CONTEXT (top related buildings from our database):\n");

        if (hits.isEmpty()) {
            sb.append("- (No matching buildings found in the database for this question.)\n\n");
            return sb.toString();
        }

        int i = 1;
        for (SearchResult r : hits) {
            Optional<Building> bOpt = buildingRepository.findById(r.buildingId());
            if (bOpt.isEmpty())
                continue;

            Building b = bOpt.get();

            StringJoiner tags = new StringJoiner(", ");
            if (b.getTags() != null && !b.getTags().isBlank())
                tags.add(b.getTags());

            sb.append(i++).append(") ")
                    .append(b.getName())
                    .append(" | dynasty=").append(nullToDash(b.getDynasty()))
                    .append(" | type=").append(nullToDash(b.getType()))
                    .append(" | location=").append(nullToDash(b.getLocation()))
                    .append("\n");

            String snippet = (r.snippet() == null || r.snippet().isBlank())
                    ? safeSnippet(b.getDescription())
                    : r.snippet();

            if (!snippet.isBlank()) {
                sb.append("   description: ").append(snippet).append("\n");
            }

            sb.append("\n");
        }

        sb.append("RESPONSE REQUIREMENTS:\n")
                .append("- Be concise and factual.\n")
                .append("- If you mention an item, refer to it by name.\n")
                .append("- Do not invent facts not present in context.\n");

        return sb.toString();
    }

    private String nullToDash(String s) {
        return (s == null || s.isBlank()) ? "-" : s.trim();
    }

    private String safeSnippet(String s) {
        if (s == null)
            return "";
        String t = s.trim();
        if (t.length() <= 220)
            return t;
        return t.substring(0, 220) + "...";
    }

    public record ChatAnswer(String promptUsed, String answer) {
    }
}
