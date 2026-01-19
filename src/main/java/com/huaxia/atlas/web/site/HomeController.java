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
        long buildingCount = buildingRepository.count();
        long approvedPostCount = postService.countApproved();
        long productCount = productRepository.count();

        model.addAttribute("featured", featured);
        model.addAttribute("buildingCount", buildingCount);
        model.addAttribute("approvedPostCount", approvedPostCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("buildingProgress", progress(buildingCount, 120));
        model.addAttribute("approvedPostProgress", progress(approvedPostCount, 12));
        model.addAttribute("productProgress", progress(productCount, 40));
        return "public/index";
    }

    private int progress(long count, int max) {
        if (max <= 0) {
            return 0;
        }
        long value = Math.round((double) count * 100 / max);
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return (int) value;
    }
}
