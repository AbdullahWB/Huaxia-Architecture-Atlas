package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.building.BuildingRepository;
import com.huaxia.atlas.domain.building.BuildingService;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.product.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BuildingService buildingService;
    private final BuildingRepository buildingRepository;
    private final PostService postService;
    private final ProductRepository productRepository;

    public HomeController(BuildingService buildingService,
                          BuildingRepository buildingRepository,
                          PostService postService,
                          ProductRepository productRepository) {
        this.buildingService = buildingService;
        this.buildingRepository = buildingRepository;
        this.postService = postService;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // show latest buildings as "featured"
        var featured = buildingService.list("", "", 0, 6).getContent();
        model.addAttribute("featured", featured);
        model.addAttribute("buildingCount", buildingRepository.count());
        model.addAttribute("approvedPostCount", postService.countApproved());
        model.addAttribute("productCount", productRepository.count());
        return "public/index";
    }
}
