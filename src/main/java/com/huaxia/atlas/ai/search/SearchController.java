package com.huaxia.atlas.ai.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String searchPage(
            @RequestParam(name = "q", required = false) String q,
            Model model) {
        String query = (q == null) ? "" : q.trim();
        List<SearchResult> results = query.isEmpty()
                ? List.of()
                : searchService.search(query, 24);

        model.addAttribute("q", query);
        model.addAttribute("results", results);
        model.addAttribute("count", results.size());

        return "public/search";
    }
}
