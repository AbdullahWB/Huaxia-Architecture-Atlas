package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.building.BuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BuildingService buildingService;

    public HomeController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // show latest buildings as "featured"
        var featured = buildingService.list("", "", 0, 6).getContent();
        model.addAttribute("featured", featured);
        return "public/index";
    }
}
