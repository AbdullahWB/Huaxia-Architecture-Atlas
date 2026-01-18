package com.huaxia.atlas.web.site;

import com.huaxia.atlas.ai.recommend.RelatedItemsService;
import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final RelatedItemsService relatedItemsService;

    public BuildingController(BuildingService buildingService, RelatedItemsService relatedItemsService) {
        this.buildingService = buildingService;
        this.relatedItemsService = relatedItemsService;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Building building = buildingService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var related = relatedItemsService.relatedTo(building, 6);

        model.addAttribute("building", building);
        model.addAttribute("related", related);
        return "public/building-detail";
    }
}
