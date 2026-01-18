package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.building.BuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExploreController {

    private final BuildingService buildingService;

    public ExploreController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/explore")
    public String explore(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "dynasty", required = false) String dynasty,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model
    ) {
        var result = buildingService.list(type, dynasty, page, size);

        model.addAttribute("type", type == null ? "" : type.trim());
        model.addAttribute("dynasty", dynasty == null ? "" : dynasty.trim());

        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);

        return "public/explore";
    }
}
