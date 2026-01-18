package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.building.BuildingLikeRepository;
import com.huaxia.atlas.domain.order.OrderRepository;
import com.huaxia.atlas.domain.post.PostLikeRepository;
import com.huaxia.atlas.domain.post.PostRepository;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {

    private final UserService userService;
    private final PostRepository postRepository;
    private final OrderRepository orderRepository;
    private final PostLikeRepository postLikeRepository;
    private final BuildingLikeRepository buildingLikeRepository;

    public UserDashboardController(UserService userService,
                                   PostRepository postRepository,
                                   OrderRepository orderRepository,
                                   PostLikeRepository postLikeRepository,
                                   BuildingLikeRepository buildingLikeRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.orderRepository = orderRepository;
        this.postLikeRepository = postLikeRepository;
        this.buildingLikeRepository = buildingLikeRepository;
    }

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        long postCount = postRepository.countByAuthorEmailIgnoreCase(user.getEmail());
        long orderCount = orderRepository.countByUserId(user.getId());
        long postLikes = postLikeRepository.countByUserId(user.getId());
        long buildingLikes = buildingLikeRepository.countByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("postCount", postCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("postLikeCount", postLikes);
        model.addAttribute("buildingLikeCount", buildingLikes);
        return "public/user-dashboard";
    }
}
